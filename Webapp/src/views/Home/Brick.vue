<template>
    <div class="div-brick">
        <label class="brick-label">{{ experiment.name }}</label>
        <div class="brick-img">{{ experiment.image }}</div>
        <label class="brick-description">{{ experiment.description }}</label>
        <el-button class="brick-button">进入实验</el-button>
    </div>
</template>

<script setup lang="ts">
const experiment = 
    {
        name: "实验1",
        description: "这是实验1的描述",
        image: "https://example.com/image1.jpg"
    };
</script>

<style scoped>
.div-brick {
    width: 100%;
    height: 360px;
    display: flex;
    flex-direction: column;

    border-style: solid;
    border-color: blue;
    border-width: 1px;
    border-radius: 2%;
}

.brick-label {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
    margin-left: 10px;
    margin-right: 10px;
    margin-top: 10px;
}

.brick-img {
    height: 50%;

    margin-left: 10px;
    margin-right: 10px;

    border-style: solid;
    border-radius: 2%;
    border-width: 1px;
    border-color: blue;
}

.brick-description {
    display: flex;
    align-items: center;
    font-size: 16px;
    margin-left: 10px;
    margin-right: 10px;
    margin-top: 10px;
}

.brick-button {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-left: 10px;
    margin-right: 10px;
    margin-top: auto;
    margin-bottom: 10px;

    color: white;
    background-color: #2563eb;
}
</style>