import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import Home from '@/views/Home.vue';
import About from '@/views/About.vue';

const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        name: 'Home',
        component: Home,
    },
    {
        path: '/About',
        name: 'About',
        component: About,
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 在这里添加路由的导航守卫
router.beforeEach((to, from, next) => {
    console.log('Navigating to:', to.path);
    next();
});

export default router;