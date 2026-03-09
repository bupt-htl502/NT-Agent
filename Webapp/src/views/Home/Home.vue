<template>
  <div class="image-container">
    <img :src="imageUrl" alt="动态图片" class="custom-image" />

    <div class="process-text">闯关流程：</div>
    <img
        :src="processUrl"
        alt="流程图"
        class="static-image"
      />
      
    <div class="button-wrapper">
      <el-button class="experiment-button" @click="goToExperiment">
        闯关开始 <span class="arrow">➜</span>
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue';
import {useRouter} from "vue-router";

const imageUrl = ref('/智能网络流量分析图片.png');
const processUrl =  ref('/流程图.png');

const router = useRouter();
const goToExperiment = async () => {
  await router.push(`/experiment/10002?title=子任务1：Wireshark/Tshark工具抓包`);
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

.static-container{
  display: flex;
  justify-content: center;
  align-items: center;
  width: 100%;
  height: 350px;
}

.static-image{
  width: 1800px;
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