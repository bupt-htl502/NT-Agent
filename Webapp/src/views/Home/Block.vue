<template>
    <div class="div-experiment">
        <div class="div-experiment-header">
            <img :src="props.icon" alt="动态 SVG" />
            <el-text class="mx-1 text">{{ props.title }}</el-text>
        </div>
        <div class="div-experiment-table">
            <el-row :gutter="10">
                <template v-for="item in experiments">
                    <el-col :span="6">
                        <Brick :name="item.name" :image="item.image" :description="item.description" :route="item.route" />
                    </el-col>
                </template>
            </el-row>
        </div>
    </div>
</template>

<script setup lang="ts">
import { defineProps } from "vue";
import Brick from "./Brick.vue";

const props = defineProps({
    icon: {
        type: String,
        default: "public/热门.svg"
    },
    title: {
        type: String,
        default: "热门实验"
    }
});
const experiments = [
    {
        name: "实验1",
        image: "https://example.com/image1.png",
        description: "这是实验1的描述",
        route: "setting"
    },
    {
        name: "实验2",
        image: "https://example.com/image2.png",
        description: "这是实验2的描述",
        route: "home"
    },
    {
        name: "实验3",
        image: "https://example.com/image3.png",
        description: "这是实验3的描述",
        route: "home"
    }
];
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

img {
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

}
</style>
