<template>
  <div class="experiment-container">
    <div class="main-card">
      <div class="card-header">
        <h2>PCAP流量文件分析工具</h2>
        <p class="subtitle">上传PCAP文件，将其转换为可视化图片</p>
      </div>

      <div v-if="loading" class="loading-state">
        <div class="spinner"></div>
        <p>正在处理{{ fileInfo?.name || 'PCAP文件' }}，请稍候...</p>
      </div>

      <div v-else-if="imageUrl" class="result-section">
        <div class="file-icon-container">
          <div class="png-icon">
            <el-icon class="icon-inner"><document /></el-icon>
            <span class="file-type">PNG</span>
          </div>
        </div>
      </div>

      <div v-else class="initial-state">
        <div v-if="errorMessage" class="error-message">
          <el-icon class="error-icon"><warning /></el-icon>
          <span>{{ errorMessage }}</span>
        </div>
        <p v-else>上传PCAP文件后，系统将自动进行处理并生成可视化结果</p>
      </div>

      <div v-if="imageUrl && !loading" class="download-section">
        <el-button
          class="download-btn"
          @click="downloadImage"
          :loading="downloading"
        >
          <el-icon v-if="!downloading"><download /></el-icon>
          <el-icon v-if="downloading"><loading /></el-icon>
          下载结果图片
        </el-button>
      </div>

      <div class="upload-section">
        <el-upload 
          v-model:file-list="pcapFiles" 
          class="upload-container" 
          accept=".pcap"
          action="/api/dify/upload" 
          :on-success="handleUploadSuccess" 
          :on-remove="handleFileRemove" 
          :limit="1"
          :disabled="loading || pcapFiles.length > 0"
          :on-error="handleUploadError"
        >
          <div class="upload-area">
            <el-icon class="upload-icon"><upload-filled /></el-icon>
            <div class="upload-text">
              <p>点击文件到此处上传</p>
              <p class="upload-hint">支持 .pcap 格式文件</p>
              <el-button 
                size="small" 
                type="warning" 
                round 
                class="upload-btn"
              >
                选择PCAP文件
              </el-button>
            </div>
          </div>
        </el-upload>
        
        <div v-if="pcapFiles.length > 0 && !loading" class="file-info">
          <el-tag closable @close="handleFileRemove">
            <el-icon class="file-icon"><document /></el-icon>
            {{ pcapFiles[0].name }}
          </el-tag>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, watch } from "vue";
import { UploadFile, ElMessage } from "element-plus";
import { DifyApi } from "@/apis/DifyApi.ts";
import { 
  UploadFilled, 
  Download, 
  Loading, 
  Warning, 
  Document,
  Check,
  Close 
} from "@element-plus/icons-vue";

interface FileInfo {
  id: string;
  name: string;
}

const pcapFiles = ref<UploadFile[]>([]);
const fileInfo = ref<FileInfo | null>(null);
const loading = ref<boolean>(false);
const downloading = ref<boolean>(false);
const imageUrl = ref<string>("");
const errorMessage = ref<string>("");
const imageLoading = ref<boolean>(true);
const answer = ref<string>("");

// 处理成功时触发绿色ElMessage
watch(imageUrl, (newVal) => {
  if (newVal && !loading.value) {
    ElMessage({
      type: "success",
      message: `PCAP文件处理成功，生成PNG图片可下载`,
      icon: Check,
      duration: 3000,
      showClose: true
    });
  }
}, { immediate: false });

// 处理失败时触发红色ElMessage
watch(errorMessage, (newVal) => {
  if (newVal && !loading.value && !imageUrl.value) {
    ElMessage({
      type: "error",
      message: newVal,
      icon: Close,
      duration: 4000,
      showClose: true
    });
  }
}, { immediate: false });

// 文件上传成功处理
const handleUploadSuccess = (response: any, uploadFile: UploadFile) => {
  if (response?.data?.id) {
    fileInfo.value = {
      id: response.data.id,
      name: uploadFile.name || 'unknown.pcap'
    };
    
    // 开始处理文件
    processFile();
  } else {
    handleError("文件上传失败，无法获取文件ID");
  }
};

// 处理上传错误
const handleUploadError = (error: any) => {
  console.error("上传错误:", error);
  handleError("文件上传失败，请重试");
};

// 文件移除处理
const handleFileRemove = () => {
  pcapFiles.value = [];
  fileInfo.value = null;
  imageUrl.value = "";
  errorMessage.value = "";
};

// 处理文件
const processFile = () => {
  if (!fileInfo.value) return;
  
  // 重置状态
  loading.value = true;
  errorMessage.value = "";
  imageUrl.value = "";
  answer.value = "";
  
  try {
    DifyApi.chat(
      {
        query: `请将该${fileInfo.value.name}流量文件转为图片。`,
        fileid: fileInfo.value.id,
      },
      (event) => {
        try {
          const obj = JSON.parse(event.data);
          
          // 处理错误事件
          if (obj.event_type === "ERROR") {
            handleError(obj.message || "处理文件时发生错误");
            answer.value = "服务器异常，请稍后再试！";
            loading.value = false;
          } 
          // 处理消息事件
          else if (obj.event_type === "MESSAGE") {
            answer.value += obj.answer; // 累积响应内容
            console.log("当前响应内容:", answer.value);
          } 
          // 处理消息结束事件
          else if (obj.event_type === "MESSAGE_END") {
            loading.value = false;
            const match = answer.value.match(/!\[.*?\]\((\/files\/tools\/.*?)\)/);
            if (match && match[1]) {
              imageUrl.value = `http://10.101.170.78${match[1]}`;
              imageLoading.value = true;
              console.log("图片下载链接:", imageUrl.value);
            } else {
              console.error("未找到图片 URL");
              handleError("未能从响应中提取图片URL");
            }
          }
        } catch (parseError) {
          console.error("解析响应失败:", parseError);
          handleError("解析服务器响应失败");
          answer.value = "服务器异常，请稍后再试！";
          loading.value = false;
        }
      },
      (error) => {
        console.error("API调用错误:", error);
        handleError("服务器连接异常，请稍后再试");
        answer.value = "服务器异常，请稍后再试！";
        loading.value = false;
      }
    );
  } catch (apiError) {
    console.error("调用Dify API失败:", apiError);
    handleError("启动文件处理失败，请重试");
    loading.value = false;
  }
};

// 下载图片
const downloadImage = async () => {
  if (!imageUrl.value || !fileInfo.value) {
    handleError("没有可下载的图片");
    return;
  }
  
  downloading.value = true;
  
  try {
    const response = await fetch(imageUrl.value);
    
    if (!response.ok) {
      throw new Error(`HTTP错误: ${response.status}`);
    }
    
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    
    // 生成文件名
    const baseName = fileInfo.value.name.replace(/\.[^/.]+$/, '');
    link.download = `${baseName}.png`;
    link.href = url;
    
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error("下载失败:", error);
    handleError("图片下载失败，请重试");
  } finally {
    downloading.value = false;
  }
};

// 错误处理通用方法
const handleError = (message: string) => {
  errorMessage.value = message;
  loading.value = false;
  downloading.value = false;
};
</script>

<style lang="scss" scoped>
.experiment-container {
  min-height: 60vh;
  padding: 5px;
  background-color: #f5f7fa;
  display: flex;
  justify-content: center;
  align-items: flex-start;
}

.main-card {
  width: 100%;
  max-width: 1200px;
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.08);
  overflow: hidden;
  transition: transform 0.3s ease, box-shadow 0.3s ease;
}

.main-card:hover {
  box-shadow: 0 8px 30px rgba(0, 0, 0, 0.12);
}

.card-header {
  padding: 24px;
  border-bottom: 1px solid #f0f2f5;
  text-align: center;
}

.card-header h2 {
  margin: 0 0 8px 0;
  color: #1d2129;
  font-size: 22px;
  font-weight: 600;
}

.subtitle {
  margin: 0;
  color: #86909c;
  font-size: 14px;
}

/* 加载状态样式 */
.loading-state {
  padding: 60px 20px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.spinner {
  width: 40px;
  height: 40px;
  border: 4px solid #f0f2f5;
  border-top: 4px solid #409eff;
  border-radius: 50%;
  animation: spin 1s linear infinite;
  margin-bottom: 16px;
}

@keyframes spin {
  0% { transform: rotate(0deg); }
  100% { transform: rotate(360deg); }
}

.loading-state p {
  color: #4e5969;
  font-size: 16px;
}

/* 结果展示区域 */
.result-section {
  padding: 24px;
}

.result-section h3 {
  margin: 0 0 16px 0;
  color: #1d2129;
  font-size: 18px;
  font-weight: 500;
}

/* PNG文件图标容器 */
.file-icon-container {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 16px;
  margin-bottom: 28px;
}

/* PNG图标样式（模拟系统文件图标） */
.png-icon {
  width: 80px;
  height: 100px;
  background-color: #f0f7ff;
  border-radius: 8px;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  position: relative;
  box-shadow: 0 2px 8px rgba(64, 158, 255, 0.15);
}

.icon-inner {
  font-size: 36px;
  color: #409eff;
  margin-bottom: 8px;
}

/* 初始状态和错误状态 */
.initial-state {
  padding: 40px 20px;
  text-align: center;
  color: #86909c;
  font-size: 16px;
}

.error-message {
  color: #f53f3f;
  padding: 12px;
  background-color: #fff1f0;
  border-radius: 6px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.error-icon {
  font-size: 18px;
}

/* 下载区域 */
.download-section {
  padding: 0 24px 24px;
  display: flex;
  justify-content: center;
}

.download-btn {
  background-color: #409eff;
  color: #fff;
  border: none;
  padding: 10px 24px;
  font-size: 16px;
  border-radius: 6px;
  display: inline-flex;
  align-items: center;
  gap: 8px;
  transition: all 0.2s ease;
}

.download-btn:hover {
  background-color: #66b1ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.2);
}

.download-btn:active {
  transform: translateY(0);
}

/* 上传区域 */
.upload-section {
  padding: 24px;
  border-top: 1px solid #f0f2f5;
  display: flex;
  flex-direction: column;
  align-items: center;
}

.upload-container {
  width: 100%;
}

.upload-area {
  border: 2px dashed #c9cdD4;
  border-radius: 8px;
  padding: 40px 415px;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
}

.upload-area:hover {
  border-color: #409eff;
  background-color: rgba(64, 158, 255, 0.03);
}

.upload-icon {
  font-size: 48px;
  color: #86909c;
  margin-bottom: 16px;
  transition: color 0.3s ease;
}

.upload-area:hover .upload-icon {
  color: #409eff;
}

.upload-text {
  margin-bottom: 20px;
}

.upload-text p {
  margin: 0 0 8px 0;
  color: #1d2129;
  font-size: 16px;
}

.upload-hint {
  color: #86909c !important;
  font-size: 14px !important;
}

.upload-btn {
  margin-top: 8px;
  padding: 8px 20px;
}

/* 已上传文件信息 */
.file-info {
  margin-top: 16px;
  align-self: flex-start;
}

.file-icon {
  margin-right: 6px;
}

/* 响应式调整 */
@media (max-width: 768px) {
  .main-card {
    margin: 0 10px;
  }
  
  .card-header,
  .result-section,
  .upload-section,
  .download-section {
    padding: 16px;
  }
  
  .upload-area {
    padding: 30px 10px;
  }
  
  .download-btn {
    width: 100%;
    justify-content: center;
  }
}
</style>
