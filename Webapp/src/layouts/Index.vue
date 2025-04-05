<template>
    <el-container>
        <el-aside width="200px">
            <AppMenu />
        </el-aside>
        <el-container>
            <el-header>
                <AppHeader />
            </el-header>
            <el-main>
                <!-- 子路由出口 -->
                <router-view />
            </el-main>
        </el-container>
    </el-container>
</template>
  
<script setup lang="ts">
import AppMenu from './AppMenu.vue'
import AppHeader from './AppHeader.vue'
</script>

<style scoped lang="scss">
.el-container {
    height: 100vh;
}

.el-header {
    background-color: #fff;
    color: #333;
    display: flex;
    justify-content: space-between;
    align-items: center;
}

.el-aside {
    width: auto;
    background-color: #304156;
}

.el-main {
    background-color: #E9EEF3;
}
</style>
  
  