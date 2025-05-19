<template>
  <div class="image-container">
    <img :src="imageUrl" alt="动态图片" class="custom-image" />
    <div class="process-text">闯关流程：</div>
    <img
        src="https://pic.616pic.com/ys_bnew_img/00/63/11/TypUb8fkVe.jpg"
        alt="静态图片"
        class="static-image"
    />
    <div class="button-wrapper">
      <el-button class="experiment-button" @click="goToExperiment">
        进入实验 <span class="arrow">➜</span>
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';

const imageUrl = ref('/智能网络流量分析图片.jpg');

// 获取cookie相关内容
function getCookie(name: string): string | number | null {
  const nameEQ = `${name}=`;
  const cookies = document.cookie.split(';');
  for (let cookie of cookies) {
    cookie = cookie.trim();
    if (cookie.startsWith(nameEQ)) {
      return decodeURIComponent(cookie.substring(nameEQ.length));
    }
  }
  return null;
}

const initializeStudent = async () => {
  const studentName = getCookie("studentName");
  const studentNo = getCookie("studentNo");
  const studentId = getCookie("studentId")

  if (studentName === null || studentNo === null || studentId === null) {
    // 跳转到注册页面
    window.location.href = "/experiment/40003"; // 替换为你的注册页面路径
  }
}

// 在组件初始化时调用
initializeStudent()

const goToExperiment = async () => {
  window.location.href = "/experiment/10002?title=%E5%9C%BA%E6%99%AF1%EF%BC%9AWireshark%E5%B7%A5%E5%85%B7%E4%BB%A5%E5%8F%8ATshark%E5%B7%A5%E5%85%B7%E6%8A%93%E5%8C%85";
};
</script>

<style>
.image-container {
  display: flex;
  flex-direction: column;
  width: 2080px;
  background-color: white;
  border-radius: 8px;
}

.custom-image{
  width: 2080px;
  height: 500px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 15px;
}

.process-text {
  font-size: 25px; /* 字体大小 */
  font-weight: 700; /* 加粗突出 */
  color: #2c3e50; /* 深色文字 */
  text-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2); /* 轻微阴影 */
  letter-spacing: 2px; /* 字间距 */
  background: linear-gradient(45deg, #ff6b6b, #4ecdc4);
  -webkit-background-clip: text;
  background-clip: text;
  color: transparent;
  margin-bottom: 2px;
}

.static-image{
  width: 2080px;
  height: 350px;
  object-fit: cover;
  border-radius: 10px;
  margin-bottom: 10px;
}

.button-wrapper {
  width: 100%;
  display: flex;
  justify-content: flex-end;
}

.experiment-button {
  width: 300px;
  height: 50px;
  background-color: #409eff;
  font-size: 20px;
  color: white;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.experiment-button:hover {
  background-color: white;
}

.experiment-button .arrow {
  margin-left: 8px;
  font-size: 20px;
  transition: transform 0.3s ease;
}

.experiment-button:hover .arrow {
  transform: translateX(5px);
}
</style>