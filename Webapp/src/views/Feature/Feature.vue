<template>
    <div class="experiment-container">
        <span class="experiment-title">{{ props.title }}</span>
        <div class="experiment-body">
            <div class="experiment-page">
                <el-table :data="tableData" class="experiment-table">
                    <el-table-column prop="name" label="特征字段" width="300">
                        <template #header>
                            <span style="color: black;">特征字段</span>
                        </template>
                        <template #default="scope">
                            <span>{{ scope.row.name }}</span>
                            <el-tooltip :disabled="disable" class="box-item" effect="dark" content="你必须首先上传pcap文件！"
                                placement="right-start">
                                <el-button style="border-style: none;" size="small" :disabled="!disable"
                                    @click="onClick(scope.$index, scope.row)">
                                    <svg t="1745544891000" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                        xmlns="http://www.w3.org/2000/svg" p-id="16587" width="32" height="32">
                                        <path
                                            d="M220.11215 950.629283c-9.570093 0-19.140187-3.190031-25.52025-6.380062-22.330218-9.570093-35.090343-31.900312-35.090342-57.420561v-159.501557H118.031153c-57.420561 0-102.080997-44.660436-102.080997-102.080997V185.021807C15.950156 127.601246 60.610592 82.94081 118.031153 82.94081h714.566978c57.420561 0 102.080997 44.660436 102.080997 102.080997V414.70405c0 19.140187-15.950156 35.090343-35.090343 35.090343s-35.090343-15.950156-35.090343-35.090343V185.021807c0-19.140187-15.950156-35.090343-35.090342-35.090343H118.031153c-19.140187 0-35.090343 15.950156-35.090343 35.090343v443.41433c0 19.140187 15.950156 35.090343 35.090343 35.090343h108.461059v213.732087l236.062305-216.922118h51.040499c19.140187 0 35.090343 15.950156 35.090342 35.090342s-15.950156 35.090343-35.090342 35.090343h-25.52025l-226.492211 207.352025c-12.760125 6.380062-25.520249 12.760125-41.470405 12.760124z"
                                            p-id="16588" fill="#13227a"></path>
                                        <path
                                            d="M784.747664 886.82866c-121.221184 0-220.11215-98.890966-220.11215-220.112149s98.890966-220.11215 220.11215-220.11215 220.11215 98.890966 220.112149 220.11215-98.890966 220.11215-220.112149 220.112149z m0-376.423676c-82.94081 0-153.121495 70.180685-153.121496 153.121496s70.180685 153.121495 153.121496 153.121495 153.121495-70.180685 153.121495-153.121495-66.990654-153.121495-153.121495-153.121496z"
                                            p-id="16589" fill="#13227a"></path>
                                        <path
                                            d="M781.557632 759.227414m-19.140187 0a19.140187 19.140187 0 1 0 38.280374 0 19.140187 19.140187 0 1 0-38.280374 0Z"
                                            p-id="16590" fill="#13227a"></path>
                                        <path
                                            d="M781.557632 708.186916c-12.760125 0-19.140187-9.570093-19.140187-19.140187V574.205607c0-12.760125 9.570093-19.140187 19.140187-19.140186 12.760125 0 19.140187 9.570093 19.140187 19.140186v111.651091c0 12.760125-9.570093 22.330218-19.140187 22.330218z"
                                            p-id="16591" fill="#13227a"></path>
                                    </svg>
                                </el-button>
                            </el-tooltip>
                        </template>
                    </el-table-column>
                    <el-table-column prop="label" label="特征名" width="200">
                        <template #header><span style="color: black;">特征名</span></template>
                    </el-table-column>

                    <el-table-column prop="description" label="备注">
                        <template #header><span style="color: black;">备注</span></template>
                    </el-table-column>

                </el-table>

                <div class="experiment-upload">
                    <label class="experiment-upload-label">上传你的pcap试试吧</label>
                    <el-upload v-model:file-list="pcapfiles" class="upload-demo experiment-upload-btn" accept=".pcap"
                        action="/api/dify/upload" :on-success="onSuccess" :on-remove="onRemove" :limit="1">
                        <el-button size="small" type="warning" round>上传pcap</el-button>
                    </el-upload>
                </div>

                <div class="experiment-upload-csv">
                  <div class="upload-box">
                    <label class="upload-label">上传CSV文件</label>
                    <el-upload
                        class="upload-csv-btn"
                        accept=".csv"
                        v-model:file-list="csvfiles"
                        action="/api/minio/upload" :on-success="onSuccess" :on-remove="onRemove" :limit="1"
                    >
                      <el-button type="primary" size="small" round>
                        <el-icon><upload /></el-icon>
                        选择CSV文件
                      </el-button>
                    </el-upload>
                    <el-button
                        type="primary"
                        @click="scoreCsv"
                    >
                      {{ '开始评分' }}
                    </el-button>
                  </div>
                  <div v-if="score !== null" class="score-result" :class="scoreResultClass">
                    <h3>您的得分: {{ score }}</h3>
                    <p>{{ scoreMessage }}</p>
                  </div>
                </div>
            </div>

            <div class="experiment-qa">
                <iframe class="experiment-agent" :src="agent_end_point" frameborder="0" />
            </div>
        </div>
    </div>

    <el-dialog v-model="resultDialogVisible" :modal="false" :destroy-on-close="true">
        <template #header="{ titleId, titleClass }">
            <h4 :id="titleId" :class="titleClass">特征信息</h4>
        </template>
        <FeatureResultDialog :fileid="fileid" :feature="feature"></FeatureResultDialog>
    </el-dialog>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import { storeToRefs } from "pinia";
import { useDifyStore } from "@/store/index";
import { SettingApi } from "@/apis/SettingApi";
import { ScoreApi } from "@/apis/ScoreApi";
import FeatureResultDialog from "./FeatureResultDialog.vue";
import { UploadFile, UploadFiles } from "element-plus";
import { ElMessage, ElLoading } from 'element-plus';

const store = useDifyStore();
const { agent_end_point } = storeToRefs(store);
const props = defineProps(["title"])
const tableData = ref([])

onMounted(() => {
    SettingApi.query({ "condition": { "key": "VUE_TRAFFIC_STATISTICS_FEATURE_FIELD" } })
        .then((res: any) => {
            tableData.value = res.map((item: any) => JSON.parse(item.value))
        })
})

// 文件上传
const pcapfiles = ref<any[]>([]);
const csvfiles = ref<any[]>([]);
const fileid = ref<string>("");
const disable = ref<boolean>(false)
const onSuccess = (response: any, _uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
    fileid.value = response.data.id;
    disable.value = true;
}
const onRemove = (_uploadFile: UploadFile, _uploadFiles: UploadFiles) => {
    fileid.value = "";
    disable.value = false;
}

const score = ref(0.0);
const scoreMessage = ref("");
const scoreResultClass = ref("");
class Commit {
  constructor(public id: number, public studentId: number, public sceneId: number, public score: number, public path: string ,public createTime: number, public isdeleted: boolean) {}
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
    const commit = new Commit(0,111, 40012, 0, `/111/40012/${csvfiles.value[0]?.name}`, getCurrentTime(), false);
    // const result = new Commit(0,111, 111, 65, 152000, false);
    const result = await ScoreApi.insert(commit) as Commit;

    // 关闭加载状态
    loadingInstance.close();

    // 处理评分结果
    score.value = result.score;
    if (score.value < 60) {
      scoreMessage.value = "不合格，请继续努力！";
      scoreResultClass.value = "fail";
    } else {
      scoreMessage.value = "恭喜您，成绩合格！";
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

// 当前的特征值
const feature = ref<any>({});
const resultDialogVisible = ref<boolean>(false);
const onClick = (_index: number, _row: any) => {
    feature.value = _row;
    resultDialogVisible.value = true;
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

</style>