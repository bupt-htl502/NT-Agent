<template>
  <el-container class="container">
    <el-aside v-if="!isHomePage">
      <AppAside v-model:drawer="drawer" />
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-info">
          <span class="info-item">姓名：{{ studentName || '未登录' }}</span>
          <span class="info-item">学工号：{{ studentNo || '未知' }}</span>
        </div>
        <el-button class="Teacherboardbutton" @click="gotoTeacherboard" v-if="isHomePage">切换至教师端</el-button>
      </el-header>
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
import { useRoute, useRouter } from "vue-router";

const route = useRoute();
const router = useRouter();
const isHomePage = computed(() => route.meta.hideSideBar === true);
const drawer = ref<boolean>(false)

const getCookie = (key: string): string | null => {
  const cookieArr = document.cookie.split('; ');
  for (const cookie of cookieArr) {
    const [name, value] = cookie.split('=');
    if (name === key) {
      return decodeURIComponent(value);
    }
  }
  return null;
};

const studentName = getCookie('studentName');
const studentNo = getCookie('studentNo');

const gotoTeacherboard = async () => {
  await router.push({
    name: "teacherboard"
  });
}
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
  align-items: center;
  border-radius: 10px;
  padding: 0 20px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  
.Teacherboardbutton {
    margin-left: auto;
    margin-right: 20px;
    
    padding: 6px 16px;
    font-size: 14px;
    font-weight: 500;
    
    background-color: #2e7d32;
    color: white;
    border-radius: 8px;
    border: none;
    box-shadow: 0 2px 4px rgba(46, 125, 50, 0.2);
    
    transition: all 0.25s ease;
    
    &:hover {
      background-color: #1b5e20;
      color: white;
      box-shadow: 0 4px 8px rgba(46, 125, 50, 0.3);
      transform: translateY(-1px);
    }
    
    &:active {
      transform: translateY(0);
      box-shadow: 0 1px 2px rgba(46, 125, 50, 0.2);
    }
    
    &:disabled {
      background-color: #c8e6c9;
      cursor: not-allowed;
      box-shadow: none;
      transform: none;
    }
  }

  .header-info {
    display: flex;
    gap: 20px;
    font-size: 14px;
    font-family: 'Segoe UI', system-ui, sans-serif;
    
    .info-item {
      padding: 6px 14px;
      border-radius: 6px;
      background-color: rgba(255, 255, 255, 0.9);
      border: 1px solid rgba(255, 255, 255, 0.5);
      box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
      transition: all 0.2s ease;
      
      &:hover {
        background-color: #ffffff;
        transform: translateY(-1px);
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
      }
    }
  }
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
