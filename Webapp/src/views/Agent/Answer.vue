<template>
    <div class="div-answer">
        <div class="div-icon">
            <el-icon size="200%">
                <svg t="1744165392383" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                    p-id="7784" width="200" height="200">
                    <path
                        d="M13.409655 628.698683l207.665982 92.473758a121.079827 121.079827 0 0 0 237.871944-11.839201L629.112095 572.062506a181.555745 181.555745 0 1 0-169.140583-185.715464L306.253888 576.990173a120.055896 120.055896 0 0 0-31.101901 13.695076L1.826437 469.029461a511.965442 511.965442 0 1 1 11.583218 159.669222z m232.304319 103.417019l57.084147 25.406285a70.587235 70.587235 0 0 0 57.34013-128.951296l-61.563845-27.454146a100.857192 100.857192 0 1 1-52.860432 130.999157z m214.257538-345.76866v4.671684-4.671684z m181.491749 135.734837a131.063153 131.063153 0 0 1-4.73568-262.126306h4.73568a131.063153 131.063153 0 0 1 0 262.126306z m0-40.317278a90.745875 90.745875 0 1 0 0-181.491749 90.745875 90.745875 0 0 0 0 181.491749z"
                        p-id="7785" fill="#1296db"></path>
                </svg>
            </el-icon>
            <label class="agent-name">智能体</label>
        </div>
        <el-text class="text">{{ props.context }}</el-text>
    </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
const props = defineProps({ context: String })
</script>

<style scoped>
.div-answer{
    width: 100%;
    min-height: 5%;
    display: flex;
    align-items: center; 
    flex-direction:column;
    /* background-color: brown; */
}
.div-icon {
    width: 100%;
    margin-right: auto;
    display: flex;
    align-items: center;
    gap: 10px;
    /* background-color: brown; */
}

.agent-name {
    font-size: large;
    font-weight: 700;
}

.text {
    max-width: 70%;
    /* min-height: 100%; */
    margin-right: auto;

    font-size: medium;
    font-weight: 700;
    word-wrap: break-word;
    word-break: break-all;
    overflow: hidden;

    border-radius: 8px;
    border-style: solid;
    border-color: gray;
    border-width: 1px;
    /* background-color: #2563eb; */
}</style>