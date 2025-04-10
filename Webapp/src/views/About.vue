<!-- src/views/About.vue -->
 
 
<template>
    <div class="vertical-layout">
        <header>
            <h1>About Page</h1>
        </header>
        <main>
            <button @click="goToOtherPage">跳转到其他页面</button>
            <p>Your main content goes here.</p>
        </main>
        <footer>
            <p>Footer content goes here.</p>
        </footer>
    </div>
</template>
<script setup lang="ts">
import { useRouter } from 'vue-router';
const router = useRouter();
const goToOtherPage = () => {
    console.log('Clicked!');
    // 在这里指定你想要跳转的路径
    router.push('/');
};
</script>
<style scoped>
.vertical-layout {
    display: flex;
    flex-direction: column;
    align-items: center;
    justify-content: space-between;
    height: 100vh;
    /* 设置整个布局高度为视口高度，以确保内容在竖屏中居中显示 */
}

header,
main,
footer {
    width: 80%;
    /* 设置宽度为80% */
    min-width: 800px;
    /* 设置最小宽度为400px */
    max-width: 1200px;
    /* 设置最大宽度为1200px */
    padding: 20px;
    box-sizing: border-box;
    text-align: center;
    background-color: #f0f0f0;
    margin: 10px 0;
}

@media screen and (max-width: 600px) {

    /* 在小屏幕上，设置宽度为100% */
    header,
    main,
    footer {
        width: 100%;
    }
}</style>