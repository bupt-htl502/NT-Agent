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
            <el-menu-item v-if="!hideCatalog" @click="openContents()">
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

        <el-tooltip class="box-item" effect="dark" content="智能问答" placement="right">
            <el-menu-item v-if="!hideCatalog" index="/IntelligentQA" @click="refresh()">
                <el-icon style="color: aliceblue;" size="200%">
                <!-- 替换为ChatRound图标 -->
                <ChatRound />
                </el-icon>
            </el-menu-item>
        </el-tooltip>
    </el-menu>
</template>

<script setup lang="ts">
import { defineModel } from 'vue';
import { ChatRound } from '@element-plus/icons-vue';

const props = defineProps({
  hideCatalog: {
    type: Boolean,
    default: false
  }
});

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
