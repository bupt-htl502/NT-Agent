<template>
    <div class="experiment-container">
        <span class="experiment-title">{{ props.title }}</span>
        <div class="experiment-body">
            <div class="experiment-page">

            </div>
            <div class="experiment-qa">
                <iframe class="experiment-agent" :src="agent_end_point" frameborder="0" />
            </div>
        </div>

    </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia"; 
import useDifyStore from "@/store/index";
const store = useDifyStore();
const { agent_end_point } = storeToRefs(store);
const props = defineProps(["title"])
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
}

.experiment-qa {
    width: 25%;
}

.experiment-agent {
    width: 100%;
    height: 100%;
}

.dify-powered-by {
    display: none !important;
}
</style>