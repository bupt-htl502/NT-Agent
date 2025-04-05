<template>
    <el-menu active-text-color="#ffd04b" background-color="#304156" class="el-menu-vertical-demo" default-active="2"
        text-color="#fff" router>
        <el-menu-item index="/">
            <!-- <Menu> 首字母要大写，否则会和浏览器原生的 <menu> 冲突 -->
            <el-icon>
                <Menu />
            </el-icon>
            <span>首页</span>
        </el-menu-item>
        <el-sub-menu index="1">
            <template #title>
                <el-icon>
                    <location />
                </el-icon>
                <span>商品</span>
            </template>
            <el-menu-item index="/product/list">
                <el-icon>
                    <Menu />
                </el-icon>
                <span>商品列表</span>
            </el-menu-item>
            <el-menu-item index="/product/category">
                <el-icon>
                    <Menu />
                </el-icon>
                <span>商品分类</span>
            </el-menu-item>
            <el-menu-item index="/product/attr">
                <el-icon>
                    <Menu />
                </el-icon>
                <span>商品规格</span>
            </el-menu-item>
        </el-sub-menu>
    </el-menu>
</template>
  
<script setup lang="ts"></script>
  
<style scoped></style>
  
  