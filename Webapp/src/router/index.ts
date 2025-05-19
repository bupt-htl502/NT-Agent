import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import experimentRoutes from './experiment';
const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        component: ()=>import('@/layouts/Index.vue'),
        redirect: "/home",
        children:[
            {
                path: "/home",
                name: "home",
                component: ()=>import('@/views/Home/Home.vue'),
                meta:{
                    title: '首页',
                    hideSideBar: true
                }
            },
            ...experimentRoutes,
            {
                path: '/agent',
                name: 'agent',
                component: ()=>import('@/views/Agent/Agent.vue'),
                meta: { hideSideBar: false },
            },
            {
                path: '/minio',
                name: 'minio',
                component: ()=>import('@/views/Minio/Minio.vue'),
                meta: { hideSideBar: false },
            },
            {
                path: '/setting',
                name: 'setting',
                component: ()=>import('@/views/Setting/Setting.vue'),
                meta: { hideSideBar: false },
            },
        ]
    },
    {
        path: '/About',
        name: 'About',
        component: ()=>import('@/views/About.vue'),
        meta: { hideSideBar: false },
    },
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 在这里添加路由的导航守卫
router.beforeEach((to, from, next) => {
    next();
});

export default router;