<template>
    <div class="div-dialog-show">
        <div v-for="(item, _) in memory" class="div-dialog-qa">
            <Question v-if="item.role == 0" :context="item.context" />
            <Answer v-if="item.role == 1" :context="item.context" />
        </div>

    </div>
    <div class="div-dialog-input">
        <el-input v-model="input" style="width: 50%" placeholder="请输入您的问题" />
        <el-button type="primary" @click="submit">发送</el-button>
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import Question from './Question.vue'
import Answer from './Answer.vue'
import Dialogue from '@/types/memory'

const input = ref('')
const memory = ref<Dialogue[]>([
    {
        role: 1,
        context: "我是“邮小率”，你的智能助手，专门帮助学生解决概率论实验相关的问题。如果你有关于概率论的概念、实验步骤或者具体问题需要解答，随时告诉我，我会尽力帮助你！",
    } as Dialogue
])
const submit = () => {
    memory.value.push({
        role: 0,
        context: input.value,
    } as Dialogue)
    // 清除对话框
    input.value = ""
}
</script>

<style scoped>
.div-dialog-show {
    width: 55%;
    height: 95%;
    display: flex;
    gap: 5px;
    flex-direction: column;
    /* background-color: aqua; */
}

.div-dialog-qa{
    width: 100%;
    min-height: 5%;
    display: flex;
    align-items: center; 
    flex-direction:column;
    /* background-color: brown; */
}
.div-dialog-input {
    width: 100%;
    height: 5%;
    display: flex;
    align-items: center;
    justify-content: center;
    gap: 10px;
    /* background-color: blue; */
}

</style>