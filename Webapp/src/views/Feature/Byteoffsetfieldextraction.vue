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
        </div>

        <div class="experiment-upload-zip">
          <div class="upload-box">
            <label class="upload-label">上传ZIP文件</label>
            <el-upload
                class="upload-zip-btn"
                accept=".zip"
                v-model:file-list="zipfiles"
                action="/api/minio/upload" :on-success="onSuccess" :on-remove="onRemove" :limit="1"
                :data="{ path: uploadpath }"
            >
              <el-button type="primary" size="small" round>
                <el-icon><upload /></el-icon>
                选择ZIP文件
              </el-button>
            </el-upload>
            <el-button
                type="primary"
                @click="scoreZip"
                class="score-btn"
            >
              {{ '开始评分' }}
            </el-button>
          </div>
          <div v-if="score !== null" class="score-result" :class="scoreResultClass">
            <h3>您的得分: {{ score }}</h3>
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
import {UploadFile, UploadFiles} from "element-plus";
import { ElMessage, ElLoading } from 'element-plus';
import axios from "axios";
import FeishuDocument from "@/views/Components/FeishuDocument.vue";
import {useRoute, useRouter} from "vue-router";

const documentUrl = ref("https://yu5fu9ktnt.feishu.cn/docx/Ye54do0xPoX5F1xeSC5cH2TSn5g?from=from_copylink");
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

// 定义 FormParam 类
class FormParam {
  id: number = 0;
  isdeleted: boolean = false;
}

// 定义 Student2Resource 类
class Student2Resource extends FormParam {
  studentId: string | number | null;
  sceneId: number;
  path: string;
  criterion: string;
  createTime: number;

  constructor(
      studentId: string | number | null,
      sceneId: number,
      path: string,
      criterion: string,
      createTime: number
  ) {
    super();
    this.studentId = studentId;
    this.sceneId = sceneId;
    this.path = path;
    this.criterion = criterion;
    this.createTime = createTime;
  }
}

// 定义 QueryParam 类
class QueryParam<T> {
  condition: T;
  offset: number = 0;
  limit: number = -1;

  constructor(condition: T) {
    this.condition = condition;
  }
}

// 文件下载
const downZip = async () =>{
  // 启动加载动画
  const loading = ElLoading.service({
    lock: true,
    text: '正在下载中请稍后...',
    background: 'rgba(0, 0, 0, 0.7)',
  });

  try {
    const studentCondition = new Student2Resource(studentid, 40007, '', '', Date.now());
    const student2resource = new QueryParam(studentCondition)
    const result= await Student2ResourceApi.query(student2resource) as Student2Resource[]
    const results2r = result[0];
    const segments = results2r.path.split('/')
    const bucketstr = segments[0]
    const pathstr = '/' + segments.slice(1).join('/')

    const downfiles = ref([
      {
        bucket: bucketstr,
        path: pathstr,
      },
    ]);

    for (const file of downfiles.value) {
      try {
        const response = await axios.get('/api/minio/download', {
          params: {
            bucket: file.bucket,
            path: file.path,
          },
          responseType: 'blob', // 确保响应类型为 blob
        });

        // 创建 Blob 对象
        const blob = new Blob([response.data], { type: response.headers['content-type'] });

        // 创建下载链接
        const downloadUrl = window.URL.createObjectURL(blob);
        const link = document.createElement('a');
        link.href = downloadUrl;

        // 设置下载文件名
        const originalPath = file.path;
        const originalFilename = originalPath.substring(originalPath.lastIndexOf('/') + 1);
        const ext = originalFilename.substring(originalFilename.lastIndexOf('.'));  // 提取后缀名
        const filename = `experiment_data${ext}`;
        link.setAttribute('download', filename);

        // 触发下载
        document.body.appendChild(link);
        link.click();

        // 清理
        document.body.removeChild(link);
        window.URL.revokeObjectURL(downloadUrl);
      } catch (error) {
        console.error('下载文件失败:', error);
      }
    }

    // 所有文件下载完成后显示成功提示
    ElMessage.success('下载完成');
  } catch (error) {
    console.error('查询文件信息失败:', error);
    ElMessage.error('无法获取文件信息，请重试！');
  } finally {
    // 无论成功或失败，关闭加载动画
    loading.close();
  }
};

// 文件上传
const zipfiles = ref<any[]>([]);
const fileid = ref<string>("");
const disable = ref<boolean>(false)
const uploadpath = ref<string>(`/${studentid}/40007/result.zip`)

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

const scoreZip = async () => {
  if (!zipfiles.value) {
    ElMessage.warning('请先选择ZIP文件');
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
    const commit = new Commit(0,studentid, 40007, 0, `studentsdata/${studentid}/40007/result.zip`, getCurrentTime(), false);
    const result = await ScoreApi.insert(commit) as CommitVO;

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
  await router.push(`/experiment/40004?title=子任务3：特征数值化与特征归一化`);
}

const goToNextPage = async () => {
  await router.push(`/experiment/40008?title=子任务2：基于PyShark的字段提取`);
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

.experiment-upload-zip {
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

  .zip-file-info {
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