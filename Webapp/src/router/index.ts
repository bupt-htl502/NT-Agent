import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import experimentRoutes from './experiment';
import { ElMessage } from 'element-plus';
import { getCookie } from "@/utils/GetCookie.ts";
import { LockApi } from "@/apis/LockApi.ts";

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
class Commit {
    constructor(public id: number, public studentId: string | number | null, public sceneId: number, public score: number, public path: string ,public createTime: number, public isdeleted: boolean) {}
}

router.beforeEach(async (to, from, next) => {
    const studentid = getCookie('studentId')
    const sceneid = Number(to.path.split('/').pop())
    const commit = new Commit(0, studentid, sceneid, 0, "", 0, false)
    const result = await LockApi.query(commit)

    if (result) {
        ElMessage.error('该页面尚未解锁，请先通过前一实验！');
        next(false); // 阻止跳转
    } else {
        next(); // 允许跳转
    }
});

export default router;