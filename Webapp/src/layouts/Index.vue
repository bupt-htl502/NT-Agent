<template>
    <el-container class="container">
        <el-aside><AppAside/></el-aside>
        <el-container>
            <el-header></el-header>
            <el-main><router-view></router-view></el-main>
        </el-container>
    </el-container>
</template>

<script setup lang="ts">
import AppAside from './AppAside.vue'
import AppHeader from './AppHeader.vue'
</script>

<style scoped lang="scss">

.container {
    height: 100vh;
    overflow: hidden;
    // background-color: #7a73f5;
}

.el-aside {
    width: 3%;
    // background-color: #7a73f5;
}
.el-header {
    height: 5%;
    background-color: #e61919;
    color: #333;
    display: flex;
    justify-content: space-between;
    align-items: center;
}


.el-main {
    height: 95%;
    background-color: #ffff;
}
</style>
  
  