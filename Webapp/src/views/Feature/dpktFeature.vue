<template>
    <div class="experiment-container">
        <span class="experiment-title">{{ props.title }}</span>
        <div class="experiment-body">
            <div class="experiment-page">
              <div class="feishu">
                <FeishuDocument :url="documentUrl" />
              </div>

              <div class="experiment-download">
                <label class="experiment-download-label">下载你的作业</label>
                <el-button type="primary" @click="downZip">下载ZIP</el-button>
                <el-button type="warning" @click="question">有问题请点这里</el-button>
                <el-dialog
                    title="特征字段解释"
                    v-model="dialogVisible"
                    width="50%"
                    :before-close="handleClose"
                >
                  <dpktFeatureDify />
                </el-dialog>
              </div>

              <div class="experiment-upload-csv">
                <div class="upload-box">
                  <label class="upload-label">上传CSV文件</label>
                  <el-upload
                      class="upload-csv-btn"
                      accept=".csv"
                      v-model:file-list="csvfiles"
                      action="/api/minio/upload" :on-success="onSuccess" :on-remove="onRemove" :limit="1"
                      :data="{ path: uploadpath }"
                  >
                    <el-button type="primary" size="small" round>
                      <el-icon><upload /></el-icon>
                      选择CSV文件
                    </el-button>
                  </el-upload>
                  <el-button
                      type="primary"
                      @click="scoreCsv"
                      class="score-btn"
                  >
                    {{ '开始评分' }}
                  </el-button>
                </div>
                <div v-if="score !== null" class="score-result" :class="scoreResultClass">
                  <h3>您的得分: {{ score.toFixed(2) }}</h3>
                  <p>{{ scoreMessage }}</p>
                </div>
              </div>

              <div class="navigation-buttons">
                <el-button
                    type="primary"
                    @click="goToLastPage"
                >
                  上一个子任务
                </el-button>
                <el-button
                    type="primary"
                    @click="goToNextPage"
                >
                  下一个子任务
                </el-button>
              </div>

            </div>

            <div class="experiment-qa">
                <iframe class="experiment-agent" :src="agent_end_point" frameborder="0" />
            </div>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref } from "vue";
import { storeToRefs } from "pinia";
import { useDifyStore } from "@/store/index";
import { ScoreApi } from "@/apis/ScoreApi";
import { Student2ResourceApi } from "@/apis/Student2ResourceApi";
import { ElMessageBox, UploadFile, UploadFiles } from "element-plus";
import { ElMessage, ElLoading } from 'element-plus';
import axios from "axios";
import FeishuDocument from "@/views/Components/FeishuDocument.vue";
import dpktFeatureDify from "./dpktFeatureDify.vue";
import {useRoute, useRouter} from "vue-router";
import {downloadExperimentData} from "@/utils/DownloadZip.ts";

const documentUrl = ref("https://yu5fu9ktnt.feishu.cn/docx/KccvdQ16Xo8TMzxjKKOcAI03n0g?from=from_copylink");
const store = useDifyStore();
const { agent_end_point } = storeToRefs(store);
const props = defineProps(["title"])

// 获取cookie相关内容
function getCookie(name: string): string | number | null {
  const nameEQ = `${name}=`;
  const cookies = document.cookie.split(';');
  for (let cookie of cookies) {
    cookie = cookie.trim();
    if (cookie.startsWith(nameEQ)) {
      return decodeURIComponent(cookie.substring(nameEQ.length));
    }
  }
  return null;
}

const initializeStudent = async () => {
  const studentName = getCookie("studentName");
  const studentNo = getCookie("studentNo");
  const studentId = getCookie("studentId")

  if (studentName === null || studentNo === null || studentId === null) {
    // 跳转到注册页面
    window.location.href = "/home"; // 替换为你的注册页面路径
  }
}

// 在组件初始化时调用
initializeStudent()

const studentid = getCookie('studentId')

const downZip = async () => {
  await downloadExperimentData(studentid as number, 40003); // 场景ID可动态传入
};

// 弹出Dify工具
const dialogVisible = ref(false);
const question = async () => {
  dialogVisible.value = true;
}
const handleClose = (done) => {
  ElMessageBox.confirm('确定要关闭吗？')
      .then(() => {
        done(); // 关闭对话框
      })
      .catch(() => {
        // 用户取消关闭
      });
};

// 文件上传并强制重命名
const csvfiles = ref<any[]>([]);
const fileid = ref<string>("");
const disable = ref<boolean>(false)
const uploadpath = ref<string>(`/${studentid}/40003/result.csv`)

const onSuccess = (response: any, _uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
  fileid.value = response.data.id;
  disable.value = true;
}
const onRemove = (_uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
  fileid.value = "";
  disable.value = false;
}

// 评估
const score = ref(0.0);
const scoreMessage = ref("");
const scoreResultClass = ref("");
class Commit {
  constructor(public id: number, public studentId: string | number | null, public sceneId: number, public score: number, public path: string ,public createTime: number, public isdeleted: boolean) {}
}
class CommitVO{
  constructor(public score: number, public remark: string) {}
}
function getCurrentTime() {
  return Date.now();
}

const scoreCsv = async () => {
  if (!csvfiles.value) {
    ElMessage.warning('请先选择CSV文件');
    return;
  }

  // 显示加载状态
  const loadingInstance = ElLoading.service({
    lock: true,
    text: '正在评分中...',
    background: 'rgba(0, 0, 0, 0.7)',
  });

  try {
    // 调用评分API
    const commit = new Commit(0,studentid, 40003, 0, `studentsdata/${studentid}/40003/result.csv`, getCurrentTime(), false);
    const result = await ScoreApi.insert(commit) as CommitVO;
    console.log('result:', result);

    // 关闭加载状态
    loadingInstance.close();

    // 处理评分结果
    score.value = result.score;
    if (score.value < 60) {
      scoreMessage.value = result.remark;
      scoreResultClass.value = "fail";
    } else {
      scoreMessage.value = result.remark;
      scoreResultClass.value = "pass";
    }

    ElMessage.success('评分完成');
  } catch (error) {
    // 关闭加载状态
    loadingInstance.close();

    ElMessage.error('评分失败: ' + (error as Error).message);
    console.error('评分出错:', error);
  }
};

// 跳转到上/下一实验
const route = useRoute();
const router = useRouter();

const goToLastPage = async () => {
  await router.push(`/experiment/40002?title=子任务1：CICFlowMeter提取统计特征`);
}

const goToNextPage = async () => {
  await router.push(`/experiment/40004?title=子任务3：特征数值化与特征归一化`);
}
</script>

<style lang="scss" scoped>
.experiment-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
}

.experiment-title {
    font-size: 20px;
    font-weight: bold;
    color: #333;
    margin-left: 20px;
    margin-bottom: 10px;
    // background-color: aqua;
}

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

.experiment-download {
    display: flex;
    align-items: center;
    margin-top: 1%;
    margin-right: 10%;
    justify-content: flex-end;
    gap: 10px;
    // background-color: aquamarine;
}

.experiment-download-label{
  font-size: 16px;
  color: gray;
}

.experiment-qa {
    width: 25%;
}

.experiment-agent {
    width: 100%;
    height: 100%;
}

.experiment-upload-csv {
   margin: 16px 10px;
   padding: 16px;
   border: 1px dashed #dcdfe6;
   border-radius: 4px;
   background-color: #f5f7fa;

   .upload-box {
     display: flex;
     align-items: center;
     gap: 12px;
   }

   .upload-label {
     font-size: 14px;
     color: #606266;
   }

   .csv-file-info {
     margin-top: 8px;
     font-size: 12px;
     color: #909399;
   }
}

.score-btn {
  font-size: 20px;
  width: 200px;
  height: 40px;
  margin-left: auto;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.score-result {
  text-align: center;
  padding: 20px;

  h3 {
    font-size: 24px;
    margin-bottom: 10px;
  }

  p {
    font-size: 18px;
  }

  &.pass {
    color: #67c23a;
    background-color: #f0f9eb;
  }

  &.fail {
    color: #f56c6c;
    background-color: #fef0f0;
  }
}

.navigation-buttons {
  display: flex;
  justify-content: center;
  margin-top: 5px;
  margin-bottom: 5px;
}

.feishu {
  width: 98%;
  height: 100%;
  border-radius: 1%;
  display: flex;
  justify-content: center;
  margin-left: 1%;
  margin-right: 1%;

  font-size: medium;

}
</style>