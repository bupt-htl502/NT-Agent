<template>
    <el-container class="container">
        <el-aside>
            <AppAside v-model:drawer="drawer" />
        </el-aside>
        <el-container>
            <el-header></el-header>
            <el-main>
                <!-- 抽屉 -->
                <el-drawer v-model="drawer" class="drawer" size="20%" title="目录" :direction="'ltr'" :append-to-body="false"
                    :destroy-on-close="true">
                    <Contents />
                </el-drawer>

                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import AppAside from './AppAside.vue'
import Contents from '@/views/Contents/Contents.vue';

const drawer = ref<boolean>(false)
</script>

<style scoped lang="scss">
.container {
    height: 100vh;
    overflow: hidden;
    // background-color: #7a73f5;
}

.el-aside {
    width: 3%;
    // background-color: #7a73f5;
}

.el-header {
    height: 5%;
    background-color: #e61919;
    color: #333;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.el-main {
    height: 0;
    position: relative;
    background-color: #ffff;

    :deep .el-overlay {
        height: 100%;
        position: absolute;
        // background-color: aqua;
    }
}

.drawer {
    height: 100%;
    overflow: hidden;
    // background-color: aqua;
}
</style>
  
  