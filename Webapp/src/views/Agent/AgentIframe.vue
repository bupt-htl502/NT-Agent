<template>
    <div class="div-iframe">
        <iframe class="iframe" :src="agent_end_point" frameborder="0" allow="microphone"/>
    </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useDifyStore } from "@/store/index";
const { agent_end_point } =  storeToRefs(useDifyStore());
</script>

<style scoped>
.div-iframe {
    width: 60%;
    height: 100%;
    display: flex;
    align-items: center;
    /* justify-content: center; */
}

.iframe {
    width: 100%;
    height: 100%;
    min-height: 700px;
}
</style>
