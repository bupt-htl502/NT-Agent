<template>
    <div class="common-layout">
        <el-container>
            <el-aside width="200px">
                <AppAside />
            </el-aside>

            <el-container>
                <el-header>
                    <AppHeader />
                </el-header>

                <el-main><router-view /></el-main>
                <el-footer>Footer</el-footer>

            </el-container>
        </el-container>
    </div>
</template>
<script setup lang="ts">
import AppAside from './AppAside.vue'
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
  
  