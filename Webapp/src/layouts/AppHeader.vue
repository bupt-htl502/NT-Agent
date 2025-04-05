<template>
    <el-space size="large">
        <el-icon>
            <component :is="store.collapse ? 'expand' : 'fold'" @click="changeCollapse" />
        </el-icon>
        <!-- 面包屑 -->
        <el-breadcrumb separator-icon="arrow-right">
            <el-breadcrumb-item v-for="item in routes" :key="item.path">
                {{ item.meta.title }}
            </el-breadcrumb-item>
        </el-breadcrumb>
    </el-space>
</template>
  
<script setup lang="ts">
import { useRouter } from 'vue-router'
import { computed } from 'vue'
import useStore from '@/store'
const store = useStore()
const changeCollapse = () => { store.collapse = !store.collapse }

// 获取路由
const router = useRouter()
const routes = computed(() => {
    return router.currentRoute.value.matched.filter(item => item.meta.title)
})
</script>
  
<style scoped lang="scss" >
i {
    font-size: 19px;
    cursor: pointer;
}
</style>
  
  