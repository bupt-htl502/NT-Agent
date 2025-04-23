<template>
    <div class="div-experiment">
        <div class="div-experiment-header">
            <svg t="1745220718075" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                p-id="4232" width="200" height="200">
                <path
                    d="M589.235892 450.06645H235.356252L371.617441 0h353.875027zM454.192432 450.06645h334.451316L342.710198 1024"
                    fill="#d81e06" p-id="4233"></path>
            </svg>
            <el-text class="mx-1 text">思维导图</el-text>
        </div>
        <div class="div-experiment-table">
            <el-row :gutter="10">
                <template v-for="item in data">
                    <el-col :span="6">
                        <Brick :name="item.name" :action="'进入思维导图'" :image="item.image" :description="item.description" :route="item.route" />
                    </el-col>
                </template>
            </el-row>
        </div>
    </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from "vue";
import Brick from "./Brick.vue";
import { SettingApi } from "@/apis/SettingApi";

const data = ref<any>([]);
onMounted(() => {
    SettingApi.query({ "condition": { "key": "VUE_MIND_MAP" } }).then((res: any[any]) => {
        data.value = res.map((item: any) => {
            let json = JSON.parse(item.value);
            return {
                name: json.name,
                image: json.image,
                description: json.description,
                route: json.route
            }
        });
    })
});

</script>
<style>
.div-experiment {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    align-items: center;
}

.div-experiment-header {
    width: 100%;
    height: 5%;
    display: flex;
    align-items: center;
    gap: 10px;

}

svg {
    width: 50px;
    height: 50px;
    display: block;
    margin-bottom: 10px;
}

.text {
    font-size: 25px;
    color: #160baf;
    font-weight: bold;
}

.div-experiment-table {
    width: 100%;
    height: 95%;
    display: flex;
    flex-direction: column;
    align-items: center;

}

.el-row {
    margin-bottom: 20px;
    width: 100%;
    height: 100%;

}

.el-row:last-child {
    margin-bottom: 0;
}

.el-col {
    height: 100%;
    border-radius: 4px;

}</style>
