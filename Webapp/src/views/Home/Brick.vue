<template>
    <div class="div-brick">
        <label class="brick-label">实验名称</label>
        <div class="brick-img">hello</div>
        <label class="brick-description">实验描述</label>
        <el-button class="brick-button">进入实验</el-button>
    </div>
</template>

<style scoped>
.div-brick {
    width: 100%;
    min-height: 360px;
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
    border-radius: 2%;
    margin-left: 10px;
    margin-right: 10px;

    border-style: solid;
    border-radius: 5%;
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