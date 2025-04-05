import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
const routes: Array<RouteRecordRaw> = [
    {
        path: '/',
        component: ()=>import('@/layouts/Index.vue'),
        redirect: "/home",
        children:[
            {
                path: "/home",
                name: "home",
                component: ()=>import('@/views/Home.vue'),
                meta:{
                    title: '首页'
                }
            }
        ]
    },
    {
        path: '/About',
        name: 'About',
        component: ()=>import('@/views/About.vue'),
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