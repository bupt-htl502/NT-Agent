<template>
    <div class="div-upload-page">
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
import { UploadFilled } from '@element-plus/icons-vue'
</script>

<style>
.div-upload-page {
    width: 100%;
    height: 100%;
    display: flex;
    align-items: center;
    justify-content: center;
    /* background-color: aquamarine; */
}
.upload{
    width: 50%;
    flex-direction:column;
    align-items: center; 
    /* background-color: aqua; */
}
</style>