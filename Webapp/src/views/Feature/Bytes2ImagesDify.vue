<template>
  <div class="experiment-body">
    <div class="experiment-page">
      <div v-if="loading">正在处理......</div>
      <div v-else>{{ answer }}</div>
      <div class="experiment-download">
        <el-button
            v-if="imageUrl"
            class="experiment-download-btn"
            @click="downloadImage"
            :disabled="loading"
        >
          下载结果图片
        </el-button>
      </div>
      <div class="experiment-upload">
        <label class="experiment-upload-label">上传你的pcap试试吧</label>
        <el-upload v-model:file-list="pcapfiles" class="upload-demo experiment-upload-btn" accept=".pcap"
                   action="/api/dify/upload" :on-success="onSuccess" :on-remove="onRemove" :limit="1">
          <el-button size="small" type="warning" round>上传pcap</el-button>
        </el-upload>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { UploadFile, UploadFiles } from "element-plus";
import {DifyApi} from "@/apis/DifyApi.ts";

// 上传文件
const pcapfiles = ref<any[]>([]);
const fileid = ref<string>("");
const filename = ref<string>("")
const disable = ref<boolean>(false)

// 文件上传成功后调用 Dify
const loading = ref<boolean>(true);
const answer = ref<String>("")
const onSuccess = (response: any, _uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
  fileid.value = response.data.id;
  filename.value = _uploadFile.name;
  disable.value = true;

  // 文件上传成功后调用 Dify
  loading.value = true;
  answer.value = ""; // 清空之前的回答
  DifyApi.chat(
      {
        query: `请将该${filename.value}流量文件转为图片。`,
        fileid: fileid.value,
      },
      (event) => {
        const obj = JSON.parse(event.data);
        if (obj.event_type === "ERROR") {
          answer.value = "服务器异常，请稍后再试！";
          loading.value = false;
        } else if (obj.event_type === "MESSAGE") {
          answer.value += obj.answer;
          console.log(answer.value);
        } else if (obj.event_type === "MESSAGE_END") {
          loading.value = false;
          // 提取图片 URL 并构造完整下载链接
          const match = answer.value.match(/!\[.*?\]\((\/files\/tools\/.*?)\)/);
          if (match && match[1]) {
            imageUrl.value = `http://10.101.170.78${match[1]}`;
            console.log("图片下载链接:", imageUrl.value);
          } else {
            console.error("未找到图片 URL");
          }
        }
      },
      (_error) => {
        answer.value = "服务器异常，请稍后再试！";
        loading.value = false;
      }
  );
};

// 文件移除时重置状态
const onRemove = (_uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
  fileid.value = "";
  filename.value = "";
  disable.value = false;
  answer.value = ""; // 清空 Dify 响应
  loading.value = false;
};

// 下载图片
const imageUrl = ref<string>("");
const downloadImage = async () => {
  if (!imageUrl.value) {
    console.error("图片 URL 为空，无法下载");
    return;
  }
  try {
    const response = await fetch(imageUrl.value);
    const blob = await response.blob();
    const url = window.URL.createObjectURL(blob);
    const link = document.createElement('a');
    link.href = url;
    link.download = filename.value.replace(/\.[^/.]+$/, '') + '.png';
    document.body.appendChild(link);
    link.click();
    document.body.removeChild(link);
    window.URL.revokeObjectURL(url);
  } catch (error) {
    console.error("下载失败:", error);
  }
};
</script>

<style lang="scss" scoped>
.experiment-body {
  display: flex;
  flex: 1;
  gap: 10px;
  // background-color: #501616;
}

.experiment-page {
  flex: 1;
  display: flex;
  flex-direction: column;
  height: 100%;
  border-style: solid;
  border-color: blue;
  border-width: 1px;
  border-radius: 1%;
  margin-left: 10px;
  margin-right: 10px;
}

.experiment-table {
  width: 98%;
  border-radius: 1%;
  display: flex;
  justify-content: center;
  margin-left: 1%;
  margin-right: 1%;

  font-size: medium;

}

.experiment-upload {
  display: flex;
  align-items: center;
  margin-top: 1%;
  margin-right: 10%;
  margin-bottom: 1%;
  justify-content: flex-end;
  gap: 10px;
  // background-color: aquamarine;
}

.experiment-upload-label {
  font-size: 16px;
  color: gray;
}

.experiment-upload-btn {
  display: flex;
  align-items: center;
}

.experiment-download{
  display: flex;
  align-items: center;
  margin-top: 1%;
  justify-content: center;
  gap: 10px;
}

.experiment-download-btn {
  width: 50%;
  display: flex;
  margin-bottom: 10px;

  background-color: #409EFF;
  color: #FFFFFF;
  border: none;
  border-radius: 16px;
  padding: 20px 10px;
  font-size: 20px;
  line-height: 1;

  cursor: pointer;
}

.experiment-download-btn:hover {
  background-color: #66B1FF;
}
</style>