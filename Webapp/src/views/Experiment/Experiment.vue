<template>
    <div class="experiment-container">
        <span class="experiment-title">实验标题</span>
        <div class="experiment-body">
            <div class="experiment-page">
                <FeishuDocument :url="url" />
            </div>
            <div class="experiment-qa">

            </div>
        </div>

    </div>
</template>

<script setup lang="ts">
import { ref } from 'vue'
import FeishuDocument from '@/views/components/FeishuDocument.vue'

const url = ref<string>('https://yu5fu9ktnt.feishu.cn/docx/FJhPdK0aMoP2SAxPRfnctQxnnqf')
</script>

<style lang="scss" scoped>
.experiment-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
}

.experiment-title {
    font-size: 20px;
    font-weight: bold;
    color: #333;
    margin-left: 20px;
    margin-bottom: 10px;
    // background-color: aqua;
}

.experiment-body {
    display: flex;
    flex: 1;
    gap: 10px;
    // background-color: #501616;
}

.experiment-page {
    flex: 1;
    border-style: solid;
    border-color: blue;
    border-width: 1px;
    border-radius: 1%;
    // background-color: #631717;
}

.experiment-qa {
    width: 25%;
    border-style: solid;
    border-color: blue;
    border-width: 1px;
    border-radius: 1%;
}
</style>