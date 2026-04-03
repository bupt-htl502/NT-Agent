<template>
  <div class="login-container">
    <div class="login-card">
      <h2 class="login-title">智能网络流量分析</h2>
      <p class="login-subtitle">请登录后访问系统</p>
      <button class="login-button" @click="handleCASLogin">
        <i class="el-icon-connection"></i>
        单点登录 (CAS)
      </button>
      <div class="login-footer">
        <p>© 2026 智能网络流量分析</p>
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ElMessage } from 'element-plus';

// 处理CAS登录
const handleCASLogin = () => {
  try {
    // 重定向到后端的CAS登录端点（使用绝对路径）
    window.location.href = 'http://10.101.162.248:5173/login/cas';
  } catch (error) {
    console.error('登录失败:', error);
    ElMessage.error('登录失败，请稍后重试');
  }
};
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 20px;
}

.login-card {
  background: white;
  border-radius: 12px;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.1);
  padding: 40px;
  width: 100%;
  max-width: 400px;
  text-align: center;
}

.login-title {
  font-size: 24px;
  font-weight: 600;
  color: #333;
  margin-bottom: 10px;
}

.login-subtitle {
  font-size: 14px;
  color: #666;
  margin-bottom: 30px;
}

.login-button {
  width: 100%;
  padding: 12px;
  background: #409EFF;
  color: white;
  border: none;
  border-radius: 6px;
  font-size: 16px;
  font-weight: 500;
  cursor: pointer;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
}

.login-button:hover {
  background: #66b1ff;
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.login-footer {
  margin-top: 30px;
  font-size: 12px;
  color: #999;
}
</style>