<template>
    <div class="div-dialog-show">
        <Question v-for="(item, index) in memory" :context="item.context"/>
    </div>
    <div class="div-dialog-input">
        <el-input v-model="input" style="width: 50%" placeholder="请输入您的问题" />
        <el-button type="primary" @click="submit">发送</el-button>
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
import Question from './Question.vue'
import Dialogue from '@/types/memory'

const input = ref('')
const memory = ref<Dialogue[]>([])
const submit = () => {
    memory.value.push({
        type: 0,
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
    flex-direction:column;
    /* background-color: aqua; */
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