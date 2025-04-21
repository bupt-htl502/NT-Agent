<template>
    <el-drawer title="I am the title" :direction="'rtl'">
        <span>Hi, there!</span>
    </el-drawer>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
const props = defineProps({
    visible: {
        type: Boolean,
        default: false
    }
});
</script>