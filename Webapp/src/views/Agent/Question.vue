<template>
    <div class="div-question">
        <el-text class="text">{{ props.context }}</el-text>
    </div>
</template>

<script setup lang="ts">
import { defineProps} from 'vue';
const props = defineProps({ context: String})
</script>

<style scoped>
.div-question{
    width: 100%;
    min-height: 5%;
    display: flex;
    /* background-color: brown; */
}
.text{
    max-width: 70%;
    min-height: 100%;
    margin-left: auto;

    font-size: medium;
    color: white;
    font-weight: 700;
    word-wrap:break-word; 
    word-break:break-all; 
    overflow: hidden;

    border-radius: 8px;
    background-color: #2563eb;
}
</style>