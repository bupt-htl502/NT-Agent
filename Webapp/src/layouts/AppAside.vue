<template>
    <el-menu :router="true">
        <el-tooltip class="box-item" effect="dark" content="主页" placement="right">
            <el-menu-item index="/home" @click="refresh()">
                <el-icon style="color: aliceblue;" size="200%">
                    <HomeFilled />
                </el-icon>
            </el-menu-item>
        </el-tooltip>

        <el-tooltip class="box-item" effect="dark" content="目录" placement="right">
            <el-menu-item @click="openContents()">
                <el-icon style="color: aliceblue;" size="200%">
                    <svg t="1745222277116" class="icon" viewBox="0 0 1024 1024" version="1.1"
                        xmlns="http://www.w3.org/2000/svg" p-id="6316" width="200" height="200">
                        <path
                            d="M278.607 830h413.786c24.792 0 24.827-38 0-38H278.607c-24.792 0-24.827 38 0 38M278.644 721h188.712c24.841 0 24.876-38 0-38H278.644c-24.841 0-24.876 38 0 38M836.109 459.423l36.325-36.415-6.525-6.541c-23.412-23.47-39.838 26.563-16.409 50.033 2.175 2.18-15.566-9.258-13.391-7.077 23.412 23.47 59.754-12.946 36.325-36.415L510.891 60.578c-23.412-23.47-59.754 12.945-36.325 36.414l361.543 362.43z"
                            fill="#ffffff" p-id="6317"></path>
                        <path
                            d="M862.675 115.143c6.788 12.452 2.216 28.06-10.21 34.862-12.428 6.802-28.005 2.221-34.793-10.23-15.779-28.945-46.016-47.393-79.22-47.393H286.53c-49.581 0-90.25 40.755-90.25 90.449v658.338c0 49.694 40.669 90.45 90.25 90.45h451.923c49.584 0 90.268-40.758 90.268-90.433V349.291c0-14.189 11.48-25.691 25.64-25.691S880 335.102 880 349.29v491.896C880 919.24 816.355 983 738.453 983H286.53C208.627 983 145 919.24 145 841.17V182.83C145 104.76 208.627 41 286.53 41h451.923c52.189 0 99.56 28.902 124.222 74.143z"
                            fill="#ffffff" p-id="6318"></path>
                    </svg>
                </el-icon>
            </el-menu-item>
        </el-tooltip>
<!-- 
        <el-tooltip class="box-item" effect="dark" content="资源管理" placement="right">
            <el-menu-item index="/minio" @click="refresh()">
                <el-icon size="200%">
                    <svg t="1744596114983" class="icon" viewBox="0 0 1024 1024" version="1.1"
                        xmlns="http://www.w3.org/2000/svg" p-id="3515" width="200" height="200">
                        <path
                            d="M855.04 385.024q19.456 2.048 38.912 10.24t33.792 23.04 21.504 37.376 2.048 54.272q-2.048 8.192-8.192 40.448t-14.336 74.24-18.432 86.528-19.456 76.288q-5.12 18.432-14.848 37.888t-25.088 35.328-36.864 26.112-51.2 10.24l-567.296 0q-21.504 0-44.544-9.216t-42.496-26.112-31.744-40.96-12.288-53.76l0-439.296q0-62.464 33.792-97.792t95.232-35.328l503.808 0q22.528 0 46.592 8.704t43.52 24.064 31.744 35.84 12.288 44.032l0 11.264-53.248 0q-40.96 0-95.744-0.512t-116.736-0.512-115.712-0.512-92.672-0.512l-47.104 0q-26.624 0-41.472 16.896t-23.04 44.544q-8.192 29.696-18.432 62.976t-18.432 61.952q-10.24 33.792-20.48 65.536-2.048 8.192-2.048 13.312 0 17.408 11.776 29.184t29.184 11.776q31.744 0 43.008-39.936l54.272-198.656q133.12 1.024 243.712 1.024l286.72 0z"
                            p-id="3516" fill="#ffffff"></path>
                    </svg>
                </el-icon>
            </el-menu-item>
        </el-tooltip> -->
    </el-menu>
</template>
  
<script setup lang="ts">
import { defineModel } from 'vue';

const drawer = defineModel("drawer");
const openContents = () => { drawer.value = !drawer.value };
const refresh = () => { drawer.value = false; };
</script>
  
<style>
.el-menu {
    overflow: hidden;
    height: 100%;
    background-color: #7a73f5;
    border-radius: 8px;
    display: flex;
    align-items: center;
    flex-direction: column;
}
</style>
