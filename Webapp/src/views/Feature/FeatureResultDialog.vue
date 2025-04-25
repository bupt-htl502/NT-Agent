<template>
    <div class="dialog-container">

    </div>
</template>

<script lang="ts" setup>
import { defineProps } from 'vue';
const { fileid } = defineProps(['fileid']);
</script>

<style lang="scss" scoped>
.dialog-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
}

.dialog-btn {
    margin: 10px 0;
    width: 80%;
    margin-left: 10%;
    height: 100px;
    font-size: 18px;
    color: #fff;
    font-weight: bold;
    background-color: #409eff;
    border-radius: 5px;
    border-style: solid;
    border-color: blue;
}
</style>