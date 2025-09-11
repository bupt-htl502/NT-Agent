import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import experimentRoutes from './experiment';
import { ElMessage } from 'element-plus';
import { LockApi } from "@/apis/LockApi.ts";
import { UserApi } from "@/apis/UserApi.ts";


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
                    hideSideBar: true,
                    role: 'student'
                }
            },
            ...experimentRoutes,
        ]
    },
    {
        path: '/teacher',
        component: ()=>import('@/layouts/TeacherLayout.vue'),
        redirect: "/teacher/teacherboard",
        meta: { role: 'teacher' },
        children: [
            {
                path: "teacherboard",
                name: "teacherboard",
                component: ()=>import('@/views/Teacher/Teacherboard.vue'),
                meta: { title: '教师端' }
            }
        ]
    },
    {
        path: '/About',
        name: 'About',
        component: ()=>import('@/views/About.vue'),
        meta: { 
            hideSideBar: false,
            role: 'public'
        },
    },
    {
        path: '/teacher',
        redirect: '/teacher/teacherboard'
    }
];

const router = createRouter({
    history: createWebHistory(),
    routes,
});

// 用户角色类型
type UserRole = 'student' | 'teacher' | null;

const getCookie = (name: string):string | number | null => {
    const nameEQ = `${name}=`;
    const cookies = document.cookie.split(';');
    for (let cookie of cookies) {
        cookie = cookie.trim();
        if (cookie.startsWith(nameEQ)) {
            return decodeURIComponent(cookie.substring(nameEQ.length));
        }
    }
    return null;
}

// 获取用户角色
const fetchUserRole = async (): Promise<UserRole> => {
    try {
        const role = await UserApi.getRole();
        return role === 'student' || role === 'teacher' ? role : null;
    } catch (error) {
        console.error('获取用户角色失败:', error);
        return null;
    }
};


// 在这里添加路由的导航守卫
class Commit {
    constructor(public id: number, public studentId: string | number | null, public sceneId: number, public score: number, public path: string ,public createTime: number, public isdeleted: boolean) {}
}
class LockResult{
    constructor(public isLocked: boolean, public parentMessage: string, public nowMessage: string) {}
}

// 路由守卫
router.beforeEach(async (to, from, next) => {
    // 获取用户角色
    const userRole = await fetchUserRole();
    
    if (userRole === 'teacher') {
        if (to.path.startsWith('/teacher/')) {
            next();
        } else {
            next('/teacher/teacherboard');
        }
        return;
    }
    
    // 学生角色需要检查实验解锁状态
    if (userRole === 'student') {
        // 处理重定向到首页
        if (to.path === '/') {
            next('/home');
            return;
        }
        
        // 检查实验解锁状态（只在特定路由下检查）
        if (to.path.startsWith('/experiment/')) {
            const studentid = getCookie('studentId');
            const sceneid = Number(to.path.split('/').pop());
            
            if (studentid && sceneid && !isNaN(sceneid)) {
                const commit = new Commit(0, studentid, sceneid, 0, "", 0, false);
                const result = await LockApi.query(commit) as LockResult;
                
                if (result.isLocked) {
                    ElMessage.error({
                        message: `该子任务尚未解锁，请先通过：<br>${result.parentMessage}/${result.nowMessage}！`,
                        dangerouslyUseHTMLString: true,
                        duration: 5000
                    });
                    next(false);
                    return;
                }
            }
        }
        
        next(); // 学生角色放行
        return;
    }
});

export default router;