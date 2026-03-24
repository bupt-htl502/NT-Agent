<template>
  <el-container class="container">
    <el-aside v-if="!isHomePage">
      <AppAside 
        v-model:drawer="drawer"
        :hide-catalog="route.meta.hideCatalog"
      />
    </el-aside>
    <el-container>
      <el-header>
        <div class="header-info">
          <span class="info-item">姓名：{{ studentName || '未登录' }}</span>
          <span class="info-item">学工号：{{ studentNo || '未知' }}</span>
          <span class="info-item">{{ displayRole || '未知' }}</span>
          <span 
            v-if="className" 
            class="info-item class-info-item" 
            ref="classInfoRef"
          >
            <!-- 显示班级名称 -->
            <span v-if="!isShowQuitBtn" @click="toggleQuitBtn">
              班级：{{ className }}
            </span>
            <!-- 显示退出班级按钮 -->
            <el-button 
              v-else 
              type="danger" 
              size="small" 
              @click="handleQuitClass"
              class="quit-class-btn"
            >
              退出班级
            </el-button>
          </span>
        </div>
        <el-button class="Teacherboardbutton" @click="gotoTeacherboard" v-if="isHomePage">切换至教师端</el-button>
        <el-button 
          class="join-class-button" 
          @click="showClassCodeDialog = true" 
          v-if="isHomePage && displayRole === '学生'"
        >
          加入班级
        </el-button>
        <el-button class="logout-button" type="danger" @click="handleLogout" v-if="isHomePage">登出</el-button>
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
            v-if="!route.meta.hideCatalog"
        >
          <Contents />
        </el-drawer>
         <el-dialog
          v-model="showClassCodeDialog"
          title="请输入教师班级码"
          width="30%"
          :close-on-click-modal="false"
          :destroy-on-close="true"
        >
          <el-input
            v-model="classCode"
            placeholder="请输入6位班级码"
            maxlength="6"
            clearable
          />
          <template #footer>
            <el-button @click="showClassCodeDialog = false; classCode = ''">取消</el-button>
            <el-button type="primary" @click="handleJoinClass" :disabled="!classCode">确认</el-button>
          </template>
        </el-dialog>
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup lang="ts">
import { ref, computed, onMounted } from 'vue'
import AppAside from './AppAside.vue'
import Contents from '@/views/Contents/Contents.vue';
import { useRoute, useRouter } from "vue-router";
import { ElMessage, ElMessageBox } from 'element-plus';
import { ClassApi } from '@/apis/ClassApi';
import { StudentApi } from '@/apis/StudentApi';

declare module 'vue-router' {
  interface RouteMeta {
    // 定义 meta 中用到的字段类型
    hideCatalog?: boolean;
    hideSideBar?: boolean;
  }
}

interface Student {
  name: string | null;
  studentNo: string | null;
  role: number | null;
  grade: number | null;
  nowScene: number | null;
  classId: number | null;
  className: string | null;
}

interface QueryParam<T> {
  condition: T;
  offset: number;
  limit: number;
}

const route = useRoute();
const router = useRouter();
const isHomePage = computed(() => route.meta.hideSideBar === true);
const drawer = ref<boolean>(false)
const showClassCodeDialog = ref<boolean>(false); // 控制弹窗显示
const classCode = ref<string>(''); // 输入的班级码
const className = ref<string>(''); // 加入班级后的班级名称
const isShowQuitBtn = ref<boolean>(false); 

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

const deleteCookie = (key: string): void => {
  console.log('删除前的Cookie:', document.cookie);
  
  const options = [
    { path: '/', domain: window.location.hostname },
    { path: '/' }, // 无domain
    { path: window.location.pathname, domain: window.location.hostname },
    { path: window.location.pathname }
  ];

  options.forEach((opt, index) => {
    const cookieStr = `${key}=; max-age=0; path=${opt.path}; ${opt.domain ? `domain=${opt.domain};` : ''} ${window.location.protocol === 'https:' ? 'secure;' : ''}`;
    document.cookie = cookieStr;
    console.log(`删除尝试${index+1}:`, cookieStr);
  });

  console.log('删除后的Cookie:', document.cookie);
};

const studentName = getCookie('studentName');
const studentNo = getCookie('studentNo');
const role = getCookie('role');

const displayRole = computed(() => {
  if (!role || isNaN(Number(role))) {
    return '未知角色';
  }

  const roleNum = Number(role);
  switch (roleNum) {
    case 100:
      return '学生';
    case 200:
      return '教师';
    case 300:
      return '管理员';
    default:
      return '未知角色';
  }
});

const getStudentClass = async () => { 
  if (!studentNo || !studentName) {
    console.log('学生学号/姓名为空，跳过班级查询');
    return;
  }

  const queryParams: QueryParam<Student> = {
    condition: {
      name: studentName,
      studentNo: studentNo,
      role: null,
      grade: null,
      nowScene: null,
      classId: null,
      className: null
    },
    offset: 0,
    limit: 1
  };

  StudentApi.query(queryParams)
    .then((response: any) => {
      if (response[0].className) {
        const classTitle = response[0].className;
        className.value = classTitle;
        document.cookie = `className=${encodeURIComponent(classTitle)}; path=/;`;
        ElMessage.success(response[0].message || '已加入班级');
      } else {
        ElMessage.error(response[0].message || '请加入班级');
      }
    })
    .catch((error) => {
      console.error('查询班级信息失败:', error);
      ElMessage.error('查询班级信息失败');
    });
};

const toggleQuitBtn = () => {
  isShowQuitBtn.value = !isShowQuitBtn.value;
};

const handleQuitClass = async () => {
  if (!studentNo) {
    ElMessage.warning('学工号为空，无法退出班级！');
    isShowQuitBtn.value = false; // 重置按钮状态
    return;
  }

  try {
    await ElMessageBox.confirm(
      '您确定要退出当前班级吗？',
      '退出班级确认',
      {
        confirmButtonText: '确认退出',
        cancelButtonText: '取消',
        type: 'warning',
        distinguishCancelAndClose: true,
        closeOnClickModal: false
      }
    );


    const response = await ClassApi.quit({ studentNo });
    if (response?.message) {
      className.value = '';
      deleteCookie('className');
      isShowQuitBtn.value = false;
      ElMessage.success(response.message || '退出班级成功');
    } else {
      ElMessage.error(response?.message || '退出班级失败');
    }
  } catch (error: any) {
    if (error === 'cancel' || error === 'close') {
      isShowQuitBtn.value = false;
      ElMessage.info('已取消退出班级操作');
    } else {
      console.error('退出班级接口调用失败:', error);
      ElMessage.error('退出班级失败，请稍后重试');
      isShowQuitBtn.value = false;
    }
  }
};

onMounted(async () => {
  if (displayRole.value === '学生' && isHomePage.value) {
    await getStudentClass();
  }
});

const gotoTeacherboard = async () => {
  await router.push({
    name: "teacherboard"
  });
}

const handleJoinClass = async () => {
  if (!studentNo) {
    ElMessage.warning('学工号为空，请先完成登录！');
    showClassCodeDialog.value = false;
    return;
  }
  if (!classCode.value.trim()) {
    ElMessage.warning('请输入班级码！');
    return;
  }

  ClassApi.join({ studentNo, classCode: classCode.value.trim() })
    .then((response: any) => {
      if (response?.className) {
        const classTitle = response.className;
        className.value = classTitle;
        showClassCodeDialog.value = false;
        ElMessage.success(response.message || '加入班级成功');
        classCode.value = '';
      } else {
        ElMessage.error(response.message || '加入班级失败');
      }
    })
    .catch((error) => {
      console.error('加入班级失败:', error);
    });
};

// 登出函数
const handleLogout = async () => {
  try {
    deleteAllCookies()

    window.location.href = 'http://10.101.162.248:5173/logout';
  } catch (error) {
    console.error('登出失败:', error);
  }
}

const deleteAllCookies = () => {
  const cookies = document.cookie.split('; ');

  cookies.forEach(cookie => {
    const cookieName = cookie.split('=')[0];
    
    document.cookie = `${cookieName}=; 
      max-age=0;
      path=/;
      domain=${window.location.hostname};
      secure=${window.location.protocol === 'https:'};
    `;
  });

  console.log('所有 Cookie 已删除');
};
</script>

<style scoped lang="scss">
.container {
    height: 100vh;
    overflow: hidden;
}

.el-aside {
    width: 3%;
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

  .join-class-button {
    margin-right: 20px;
    padding: 6px 16px;
    font-size: 14px;
    font-weight: 500;
    background-color: #1976d2;
    color: white;
    border-radius: 8px;
    border: none;
    box-shadow: 0 2px 4px rgba(25, 118, 210, 0.2);
    transition: all 0.25s ease;
    
    &:hover {
      background-color: #1565c0;
      color: white;
      box-shadow: 0 4px 8px rgba(25, 118, 210, 0.3);
      transform: translateY(-1px);
    }
    
    &:active {
      transform: translateY(0);
      box-shadow: 0 1px 2px rgba(25, 118, 210, 0.2);
    }
  }

  .logout-button {
    padding: 6px 16px;
    font-size: 14px;
    font-weight: 500;
    border-radius: 8px;
    border: none;
    transition: all 0.25s ease;
    
    &:hover {
      box-shadow: 0 4px 8px rgba(229, 57, 53, 0.3);
      transform: translateY(-1px);
    }
    
    &:active {
      transform: translateY(0);
      box-shadow: 0 1px 2px rgba(229, 57, 53, 0.2);
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

    .class-info-item {
      position: relative;
      cursor: pointer;
      
      &:hover {
        background-color: #f8f9fa;
      }
      
      .quit-class-btn {
        padding: 4px 12px;
        font-size: 16px;
        border-radius: 6px;
        &:hover {
          background-color: #e64949;
        }
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
    }
}

.drawer {
    height: 100%;
    overflow: hidden;
}
</style>