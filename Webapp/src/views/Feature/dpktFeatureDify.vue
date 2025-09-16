<template>
  <div class="feature-analysis-container">
    <div class="page-header">
      <h1>流量特征分析工具</h1>
      <p class="header-desc">上传PCAP文件后，可查看各特征字段的详细分析结果</p>
    </div>

    <div class="main-content">
      <div v-if="!disable" class="upload-tip">
        <el-icon class="tip-icon"><info-filled /></el-icon>
        <span>请先上传PCAP文件，才能查看特征详情</span>
      </div>

      <div class="table-wrapper">
        <el-table 
          :data="tableData" 
          class="feature-table"
          :loading="tableLoading"
          border
          stripe
          empty-text="暂无特征数据，请稍后重试"
        >
          <el-table-column 
            prop="name" 
            label="特征字段" 
            width="300"
            align="center"
          >
            <template #header>
              <span class="table-header-text">特征字段</span>
            </template>
            <template #default="scope">
              <div class="feature-field-cell">
                <span class="field-name">{{ scope.row.name }}</span>
                
                <el-tooltip 
                  :disabled="disable" 
                  effect="dark" 
                  content="请先上传PCAP文件"
                  placement="right"
                  :enterable="false"
                >
                  <el-button 
                    class="detail-btn" 
                    size="small" 
                    :disabled="!disable"
                    @click="handleViewDetail(scope.row)"
                    icon="View"
                  ></el-button>
                </el-tooltip>
              </div>
            </template>
          </el-table-column>

          <el-table-column 
            prop="label" 
            label="特征名" 
            width="200"
            align="center"
          >
            <template #header>
              <span class="table-header-text">特征名</span>
            </template>
            <template #default="scope">
              <el-tag type="info" size="small">{{ scope.row.label }}</el-tag>
            </template>
          </el-table-column>

          <el-table-column 
            prop="description" 
            label="备注"
            align="left"
          >
            <template #header>
              <span class="table-header-text">备注</span>
            </template>
            <template #default="scope">
              <p class="description-text">{{ scope.row.description || '无备注信息' }}</p>
            </template>
          </el-table-column>
        </el-table>
      </div>

      <div class="upload-area">
        <el-upload 
          v-model:file-list="pcapFiles" 
          class="pcap-uploader" 
          accept=".pcap"
          action="/api/dify/upload" 
          :on-success="handleUploadSuccess" 
          :on-remove="handleFileRemove" 
          :limit="1"
          :disabled="uploadDisabled"
          :on-error="handleUploadError"
          :before-upload="handleBeforeUpload"
        >
          <el-button 
            size="small" 
            type="warning" 
            round 
            class="upload-btn"
            :loading="uploadLoading"
          >
            <el-icon v-if="!uploadLoading"><upload-filled /></el-icon>
            <el-icon v-if="uploadLoading"><loading /></el-icon>
            {{ pcapFiles.length > 0 ? '替换PCAP文件' : '上传你的PCAP试试吧' }}
          </el-button>

          <template #file="scope">
            <div class="uploaded-file-info">
              <el-icon class="file-icon"><document /></el-icon>
              <span class="file-name">{{ scope.file.name }}</span>
              <el-button 
                type="text" 
                size="small" 
                class="remove-file-btn"
                @click="handleFileRemove(scope.file, [scope.file])"
              >
                <el-icon><close /></el-icon>
              </el-button>
            </div>
          </template>
        </el-upload>
      </div>
    </div>

    <!-- 弹窗：合并原 DpktFeatureResultDialog 模板 -->
    <el-dialog 
      v-model="resultDialogVisible" 
      :modal="true"
      modal-class="dialog-modal"
      :title="`特征详情：${feature.label || feature.name || '未知特征'}`"
      :width="dialogWidth"
      :destroy-on-close="true"
      :close-on-click-modal="false"
      @close="handleDialogClose"
    >
      <!-- 加载状态（合并子组件的 loading 逻辑） -->
      <div v-if="difyLoading" class="dialog-loading">
        <div class="spinner"></div>
        <p>加载特征数据中...</p>
      </div>

      <!-- 错误状态（复用父组件 dialogError） -->
      <div v-else-if="dialogError" class="dialog-error">
        <p class="error-text">{{ dialogError }}</p>
        <el-button 
          size="small" 
          type="primary" 
          class="reload-btn"
          @click="handleReloadFeature"
        >
          重新加载
        </el-button>
      </div>

      <div v-else class="dialog-container">
        <div class="dialog-content">
          <el-text 
            class="mx-1 indented" 
            size="large" 
            v-html="answer"
          ></el-text>
        </div>
        <div class="dialog-tip">
          <el-button 
            size="small" 
            type="text"
            @click="handleReloadFeature"
          >
            <el-icon><refresh /></el-icon> 重新分析
          </el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted, computed } from "vue";
import { UploadFile, UploadFiles, UploadRawFile, ElMessage, ElMessageBox } from "element-plus";
import { SettingApi } from "@/apis/SettingApi";
import { DifyApi } from "@/apis/DifyApi"; // 导入 DifyApi（原属于子组件）
import { 
  InfoFilled, 
  UploadFilled, 
  Loading, 
  Document, 
  Close,
  Refresh
} from "@element-plus/icons-vue";

interface FeatureItem {
  name: string;
  label: string;
  description?: string;
  [key: string]: any;
}

interface UploadResponse {
  code: number;
  data: {
    id: string;
  };
  message?: string;
}

// 表格数据相关
const tableData = ref<FeatureItem[]>([]);
const tableLoading = ref<boolean>(true);
const tableError = ref<string>("");

// 文件上传相关
const pcapFiles = ref<UploadFile[]>([]);
const fileId = ref<string>("");
const uploadLoading = ref<boolean>(false);
const uploadError = ref<string>("");

// 按钮与交互状态
const disable = ref<boolean>(false);
const uploadDisabled = computed(() => uploadLoading.value || pcapFiles.value.length > 0);

// 弹窗相关
const resultDialogVisible = ref<boolean>(false);
const feature = ref<FeatureItem>({ name: "", label: "" });
const dialogLoading = ref<boolean>(false);
const difyLoading = ref<boolean>(false);
const dialogError = ref<string>("");
const answer = ref<string>("");
const dialogWidth = computed(() => {
  return window.innerWidth < 768 ? '90%' : '60%';
});

onMounted(() => {
  fetchFeatureTableData();
});

/** 获取特征表格数据 */
const fetchFeatureTableData = async () => {
  tableLoading.value = true;
  try {
    const res = await SettingApi.query({ 
      condition: { key: "VUE_TRAFFIC_STATISTICS_FEATURE_FIELD" } 
    });
    
    if (Array.isArray(res)) {
      tableData.value = res.map((item: any) => {
        try {
          return JSON.parse(item.value);
        } catch (parseErr) {
          console.error('解析特征数据失败：', parseErr, item);
          return { name: '数据异常', label: '数据异常', description: '特征数据格式错误' };
        }
      });
      tableError.value = "";
    } else {
      throw new Error('返回数据不是数组格式');
    }
  } catch (err: any) {
    tableError.value = err.message || '加载特征数据失败，请刷新页面重试';
    tableData.value = [];
    console.error('获取特征数据异常：', err);
  } finally {
    tableLoading.value = false;
  }
};

const handleBeforeUpload = (rawFile: UploadRawFile) => {
  const fileName = rawFile.name.toLowerCase();
  const isPcap = fileName.endsWith('.pcap');
  const maxSize = 200 * 1024 * 1024;
  const isLt200M = rawFile.size <= maxSize;

  if (!isPcap) {
    showErrorDialog('请上传后缀为 .pcap 的文件');
    return false;
  }
  if (!isLt200M) {
    showErrorDialog(`文件大小不能超过 200MB，当前文件大小：${(rawFile.size / 1024 / 1024).toFixed(2)}MB`);
    return false;
  }

  uploadLoading.value = true;
  return true;
};

/** 文件上传成功 */
const handleUploadSuccess = (response: UploadResponse, _uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
  uploadLoading.value = false;
  const isSuccess = [200, 201].includes(response.code) || !response.code;
  if (isSuccess && response.data?.id) {
    fileId.value = response.data.id;
    disable.value = true;
    showSuccessMessage('PCAP文件上传成功，可查看特征详情');
  } else {
    throw new Error(response.message || '上传成功但未获取文件ID');
  }
};

/** 文件上传失败 */
const handleUploadError = (error: Error) => {
  uploadLoading.value = false;
  uploadError.value = error.message || '文件上传失败，请重试';
  showErrorDialog(uploadError.value);
};

/** 处理文件移除 */
const handleFileRemove = (_uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
  fileId.value = "";
  disable.value = false;
  pcapFiles.value = [];
  showSuccessMessage('PCAP文件已移除');
};

/** 处理查看特征详情 */
const handleViewDetail = (row: FeatureItem) => {
  feature.value = row;
  resultDialogVisible.value = true;
  dialogError.value = "";
  answer.value = "";
  difyLoading.value = true;
  fetchDifyFeatureData();
};

/** 调用 Dify API 获取特征分析结果 */
const fetchDifyFeatureData = async () => {
  try {
    // 拼接查询参数（避免 undefined，添加兜底）
    const featureLabel = feature.value.label || feature.value.name || "未知";
    const queryStr = `请对这个pcap文件提取${featureLabel}特征，并解释该特征的含义。`;
    console.log("当前请求的 query 参数：", queryStr);

    // 调用 Dify API
    DifyApi.chat({
      query: queryStr,
      fileid: fileId.value,
    }, (event) => {
      let obj = JSON.parse(event.data);
      if (obj.event_type === "ERROR") {
        dialogError.value = "服务器异常，请稍后再试！";
        answer.value = "";
      }
      if (obj.event_type === "MESSAGE") {
        answer.value += obj.answer;
      }
      difyLoading.value = false;
    }, (_error) => {
      dialogError.value = "服务器异常，请稍后再试！";
      answer.value = "";
      difyLoading.value = false;
    });
  } catch (error) {
    dialogError.value = "特征分析失败，请重试！";
    difyLoading.value = false;
    console.error("Dify API 调用异常：", error);
  }
};

/** 重新加载特征数据 */
const handleReloadFeature = () => {
  dialogError.value = "";
  answer.value = "";
  difyLoading.value = true;
  fetchDifyFeatureData();
};

/** 处理弹窗关闭 */
const handleDialogClose = () => {
  feature.value = { name: "", label: "" };
  dialogLoading.value = false;
  difyLoading.value = false;
  dialogError.value = "";
  answer.value = "";
  resultDialogVisible.value = false;
};

/** 显示成功提示 */
const showSuccessMessage = (message: string) => {
  ElMessage({
    type: 'success',
    message,
    duration: 2000,
    showClose: true
  });
};

/** 显示错误弹窗 */
const showErrorDialog = (message: string) => {
  ElMessageBox.alert(
    `<p class="error-content">${message}</p>`,
    "操作提示",
    {
      type: "error",
      showClose: true,
      closeOnClickModal: false,
    }
  );
};
</script>

<style lang="scss" scoped>
.feature-analysis-container {
  min-height: 80vh;
  padding: 24px;
  background-color: #f5f7fa;
  font-family: 'Inter', 'Microsoft YaHei', sans-serif;
}

.page-header {
  margin-bottom: 24px;
  text-align: center;

  h1 {
    font-size: 24px;
    font-weight: 600;
    color: #1d2129;
    margin: 0 0 8px 0;
  }

  .header-desc {
    font-size: 14px;
    color: #86909c;
    margin: 0;
  }
}

.main-content {
  background-color: #fff;
  border-radius: 12px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
  padding: 24px;
}

.upload-tip {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 12px 16px;
  background-color: #f0f7ff;
  border-left: 4px solid #409eff;
  border-radius: 4px;
  margin-bottom: 20px;

  .tip-icon {
    color: #409eff;
    font-size: 16px;
  }

  span {
    font-size: 14px;
    color: #4e5969;
  }
}

.table-wrapper {
  margin-bottom: 24px;
  overflow: hidden;
  border-radius: 8px;
  border: 1px solid #f0f2f5;
}

.feature-table {
  width: 100%;
  border: none;

  .table-header-text {
    font-size: 15px;
    font-weight: 500;
    color: #1d2129;
  }

  .el-table__cell {
    padding: 14px 8px;
    font-size: 14px;
  }

  .feature-field-cell {
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 12px;
  }

  .field-name {
    color: #1d2129;
    font-weight: 400;
  }

  .detail-btn {
    width: 32px;
    height: 32px;
    padding: 0;
    border-radius: 50%;
    color: #409eff;
    background-color: #f0f7ff;
    transition: all 0.2s ease;

    &:hover {
      color: #fff;
      background-color: #409eff;
    }

    &:disabled {
      color: #c9cdD4;
      background-color: #f5f7fa;
      cursor: not-allowed;
    }
  }

  .el-tag {
    padding: 4px 8px;
    font-size: 13px;
  }

  .description-text {
    color: #4e5969;
    margin: 0;
    line-height: 1.5;
    word-break: break-word;
  }
}

.upload-area {
  display: flex;
  justify-content: flex-end;
  align-items: center;
}

.pcap-uploader {
  display: flex;
  align-items: center;
  gap: 12px;
}

.upload-btn {
  padding: 8px 20px;
  font-size: 14px;
  transition: all 0.2s ease;

  &:hover {
    transform: translateY(-2px);
    box-shadow: 0 4px 12px rgba(250, 173, 20, 0.2);
  }

  &:disabled {
    cursor: not-allowed;
    transform: none;
    box-shadow: none;
  }
}

.uploaded-file-info {
  display: flex;
  align-items: center;
  gap: 8px;
  padding: 6px 12px;
  background-color: #f5f7fa;
  border-radius: 4px;

  .file-icon {
    color: #86909c;
    font-size: 16px;
  }

  .file-name {
    font-size: 13px;
    color: #4e5969;
    max-width: 200px;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .remove-file-btn {
    color: #f53f3f;
    padding: 0;
    font-size: 14px;

    &:hover {
      color: #d4380d;
    }
  }
}

.dialog-loading {
  padding: 40px 0;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;

  .spinner {
    width: 36px;
    height: 36px;
    border: 4px solid #f0f2f5;
    border-top: 4px solid #409eff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 12px;
  }

  p {
    color: #4e5969;
    font-size: 14px;
    margin: 0;
  }
}

.dialog-modal {
  background-color: rgba(0, 0, 0, 0.1);
  backdrop-filter: blur(2px);
}

.error-content {
  font-size: 14px;
  color: #4e5969;
  line-height: 1.5;
  margin: 0;
}

/* 弹窗内容容器 */
.dialog-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  min-height: 150px;
}

/* 结果内容区 */
.dialog-content {
  margin: 0 3%;
  padding: 10px 0;
  flex: 1; /* 占满剩余空间 */
}

/* 文本缩进样式 */
.indented {
  display: block;
  text-indent: 2em;
  line-height: 1.8; /* 行高优化可读性 */
  color: #1d2129;
  white-space: pre-wrap; /* 保留换行符 */
}

/* 错误提示区 */
.dialog-error {
  padding: 40px 0;
  text-align: center;

  .error-text {
    color: #f53f3f;
    font-size: 14px;
    margin-bottom: 16px;
  }

  .reload-btn {
    padding: 6px 16px;
    font-size: 13px;
  }
}

/* 重新分析按钮区 */
.dialog-tip {
  display: flex;
  align-items: center;
  justify-content: flex-end;
  margin: 10px 3% 0 0;
  color: #86909c;
  font-size: 13px;

  .el-button {
    color: #409eff;

    &:hover {
      color: #2563eb;
      background-color: #f0f7ff;
    }
  }
}

@keyframes spin {
  0% {
    transform: rotate(0deg);
  }
  100% {
    transform: rotate(360deg);
  }
}

@media (max-width: 768px) {
  .feature-analysis-container {
    padding: 12px;
  }

  .main-content {
    padding: 16px;
  }

  .page-header h1 {
    font-size: 20px;
  }

  .upload-area {
    justify-content: center;
    margin-top: 16px;
  }

  .pcap-uploader {
    flex-direction: column;
    gap: 8px;
    width: 100%;
  }

  .upload-btn {
    width: 100%;
  }

  .uploaded-file-info {
    width: 100%;
    justify-content: space-between;
  }

  .file-name {
    max-width: 100%;
  }

  /* 移动端弹窗文本调整 */
  .indented {
    text-indent: 1.5em;
    font-size: 13px;
  }
}
</style>