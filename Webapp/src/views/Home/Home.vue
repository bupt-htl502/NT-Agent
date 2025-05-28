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
      <el-button class="teacher-download-student-info" @click="downloadStudentInfo">下载成绩单</el-button>
      <el-button class="experiment-register-button" @click="register">注册</el-button>
      <el-button class="experiment-button" @click="goToExperiment">
        闯关开始 <span class="arrow">➜</span>
      </el-button>
    </div>
  </div>
</template>

<script setup lang="ts">
import { ref } from 'vue';
import { StudentApi  } from "@/apis/StudentApi";
import {ElMessage} from "element-plus";
import {useRoute, useRouter} from "vue-router";
import {TeacherApi} from "@/apis/TeacherApi.ts";
import axios from "axios";

const imageUrl = ref('/智能网络流量分析图片.png');

// 注册
class Student {
  constructor(public id: number, public name: string, public studentNo: string, public role: number, public grade: number , public isdeleted: boolean,public nowScene : number) {}
}

// 设置 cookie 的辅助函数
function setCookie(name: string, value: string | number, days: number = 365) {
  const expires = new Date();
  expires.setTime(expires.getTime() + days * 24 * 60 * 60 * 1000);
  document.cookie = `${name}=${encodeURIComponent(value)};expires=${expires.toUTCString()};path=/`;
}

const studentId = ref(0)
const register = async () =>{
  try {
    const student = new Student(0,"xyq","2023140634", 100, 0, false,10000) // 后续替换为注册页面的接口，拿到用户姓名跟学号
    setCookie("studentName", student.name);
    setCookie("studentNo", student.studentNo);
    const result = await StudentApi.testModeInsert(student) as Student
    studentId.value = result.id
    setCookie("studentId", result.id)
    ElMessage.success('注册成功！');// 注册成功提示
    window.location.href = "/home";
  } catch (error) {
    console.error('注册失败:', error);
    ElMessage.error('注册失败，请重试！'); // 错误提示
  }
};
const downloadStudentInfo = async () => {
  const response = await fetch('/api/transcript/getScript?studentId=167');
  const blob = await response.blob();
  const url = URL.createObjectURL(blob);
  const link = document.createElement('a');
  link.href = url;
  link.setAttribute('download', '学生成绩单.csv');
  document.body.appendChild(link);
  link.click();
  document.body.removeChild(link);
  URL.revokeObjectURL(url);
};

const router = useRouter();
const goToExperiment = async () => {
  await router.push(`/experiment/10002?title=子任务1：Wireshark工具以及Tshark工具抓包`);
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

.teacher-download-student-info {
  width: 300px;
  height: 50px;
  background-color: #409eff;
  font-size: 20px;
  color: white;
  border-radius: 6px;
  transition: background-color 0.3s;
}

.experiment-register-button{
  width: 300px;
  height: 50px;
  background-color: #409eff;
  font-size: 20px;
  color: white;
  border-radius: 6px;
  transition: background-color 0.3s;
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