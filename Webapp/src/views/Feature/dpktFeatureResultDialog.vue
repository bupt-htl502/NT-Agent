<template>
    <div class="dialog-container">
        <div class="dialog-content">
            <el-text class="mx-1 indented" size="large" v-loading="loading" element-loading-text="Loading...">{{ answer
            }}</el-text>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, defineProps, onMounted } from 'vue';
import { DifyApi } from "@/apis/DifyApi";

const { fileid, feature } = defineProps(['fileid', "feature"]);
const loading = ref<boolean>(true);
const answer = ref<String>("")

onMounted(() => {
    DifyApi.chat({
        query: "请对这个pcap文件提取" + feature.name + "特征，并解释该特征的含义。",
        fileid: fileid,
    }, (event) => {
        let obj = JSON.parse(event.data);
        if(obj.event_type == "ERROR"){
            answer.value = "服务器异常，请稍后再试！"
        }
        if(obj.event_type == "MESSAGE"){
            answer.value += obj.answer
        }
        loading.value = false;
    }, (_error)=>{
        answer.value = "服务器异常，请稍后再试！"
        loading.value = false;
    });
});
</script>

<style lang="scss" scoped>
.dialog-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
}

.dialog-content {
    margin-left: 3%;
    margin-right: 3%;
    min-height: 100px;
    // background-color: aqua;
}

.indented {
    display: block;
    text-indent: 2em;
}

.dialog-label {
    font-size: 16px;
    color: gray;
}

.dialog-tip {
    display: flex;
    align-items: center;
    flex-direction: row;
    margin-top: 1%;
    margin-left: auto;
    margin-right: 3%;
}
</style>