<template>
    <el-text class="text">{{ props.context }}</el-text>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
const props = defineProps({ context: String })
</script>

<style scoped>
.text {
    max-width: 70%;
    min-height: 100%;
    margin-left: auto;

    font-size: medium;
    color: white;
    font-weight: 700;
    word-wrap: break-word;
    word-break: break-all;
    overflow: hidden;

    border-radius: 8px;
    background-color: #2563eb;
}
</style>