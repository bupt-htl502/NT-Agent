<template>
  <el-container class="container">
    <el-aside v-if="!isHomePage">
      <AppAside v-model:drawer="drawer" />
    </el-aside>
    <el-container>
      <el-header></el-header>
      <el-main>
        <!-- 抽屉 -->
        <el-drawer
            v-model="drawer"
            class="drawer"
            size="20%"
            title="目录"
            direction="ltr"
            :append-to-body="false"
            :destroy-on-close="true"
        >
          <Contents />
        </el-drawer>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref,computed } from 'vue'
import AppAside from './AppAside.vue'
import Contents from '@/views/Contents/Contents.vue';
import {useRoute} from "vue-router";

const route = useRoute();
const isHomePage = computed(() => route.meta.hideSideBar === true);
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
    background-color: #B3D4FF;
    color: #333;
    display: flex;
    justify-content: space-between;
    align-items: center;
    border-radius: 10px;
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
