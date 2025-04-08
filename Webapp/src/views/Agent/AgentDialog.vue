<template>
    <div class="div-dialog-show">This is a dialog</div>
    <div class="div-dialog-input">
        <el-input v-model="input" style="width: 640px" placeholder="请输入您的问题" />
        <el-button type="primary" @click="submit">发送</el-button>
    </div>
</template>
<script setup lang="ts">
import { ref } from 'vue'
const input = ref('')
const submit = () => {
    console.log(input.value)
}
</script>

<style scoped>
.div-dialog-show {
    width: 100%;
    height: 95%;
    background-color: aqua;
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