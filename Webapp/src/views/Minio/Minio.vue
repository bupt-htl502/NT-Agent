<template>
    <div class="div-upload-page">
        <div class="div-header">
            <span style="font-size: large;">分类：</span>
            <el-select v-model="bucket" placeholder="Select" style="width: 240px">
                <el-option v-for="item in options" :value="item" :disabled="item == 'temporary'" />
            </el-select>
        </div>
        <div class="div-list">
            <el-button type="primary" :loading=false
                @click="downloadZip('datasets', ['experiment/1ERLiSkGzlE-2.pcap', 'experiment/featureExtraction/1p4qnRbnVTY-1.pcap'])">Loading</el-button>
        </div>
        <el-upload class="upload-demo upload" drag action="/api/minio/upload">
            <el-icon class="el-icon--upload"><upload-filled /></el-icon>
            <div class="el-upload__text">拖拽至此 或 <em>点击上传</em></div>
            <template #tip>
                <div class="el-upload__tip" style="text-align: center;">文件大小不超过50MB</div>
            </template>
        </el-upload>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import { UploadFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus';
import { MinioApi } from '@/apis/MinioApi';
import { save, blob } from '@/utils/FileUtils'
import JSZip from 'jszip'
import { saveAs } from "file-saver";

const options = ref<String[]>([])

// 选择项
const bucket = ref<String>("")

onMounted(() => { reload() })

const reload = () => {
    MinioApi.listBucketNames().then((res) => { options.value = res as String[] })
}

const download = (bucket: String, path: String) => {
    MinioApi.download({ "bucket": bucket, "path": path })
        .then((res: any) => { save(res, "1.bin") })
        .catch((_err) => { ElMessage({ message: '文件下载失败，请稍后重试！', type: 'error' }) })
}

const downloadZip = (bucket: String, paths: String[]) => {
    const zip = new JSZip()
    const promiess = paths.map((item) => {
        return MinioApi.download({ "bucket": bucket, "path": item })
            .then((res: any) => { return blob(res) })
            .then((res: any) => { zip.file(res['filename'], res['blob'], { binary: true }) })
    })
    Promise.all(promiess).then(() => {
        zip.generateAsync({ type: 'blob' })
            .then((content) => { saveAs(content, '附件') })
            .catch((_err) => { ElMessage({ message: '文件下载失败，请稍后重试！', type: 'error' }) })
    })
}
</script>

<style>
.div-upload-page {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    /* justify-content: center; */
    flex-direction: column;
    /* background-color: aquamarine; */
}

.div-header {
    width: 80%;
    /* height: 100%; */
    /* background-color: brown; */
}

.upload {
    width: 50%;
    flex-direction: column;
    align-items: center;
    /* background-color: aqua; */
}
</style>