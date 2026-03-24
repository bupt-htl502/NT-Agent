import { createRouter, createWebHistory, RouteRecordRaw } from 'vue-router';
import experimentRoutes from './experiment';
import { ElMessage } from 'element-plus';
import { UserApi } from "@/apis/UserApi.ts";


const routes: Array<RouteRecordRaw> = [
    {
        path: '/login',
        name: 'login',
        component: ()=>import('@/views/Login/Login.vue'),
        meta:{
            title: '登录',
            hideSideBar: true,
            role: 'public'
        }
    },
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
                    role: 'public'
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
        path: '/IntelligentQA',
        component: ()=>import('@/layouts/Index.vue'),
        children:[
            {
                path: "/IntelligentQA",
                name: "IntelligentQA",
                component: ()=>import('@/views/IntelligentQA/IntelligentQA.vue'),
                meta:{
                    title: '智能问答',
                    hideSideBar: false,
                    role: 'public',
                    hideCatalog: true
                }
            },
        ]
    },
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
        const studentName = getCookie('studentName');
        const studentNo = getCookie('studentNo');
        // 准备请求参数
        const params = {};
        if (studentName) Object.assign(params, { studentName });
        if (studentNo) Object.assign(params, { studentNo });
        const role = await UserApi.getRole(params);
        return role === 'student' || role === 'teacher' ? role : null;
    } catch (error) {
        console.error('获取用户角色失败:', error);
        return null;
    }
};


// 检查用户是否已登录（通过cookie）
const isUserLoggedIn = (): boolean => {
    const studentName = getCookie('studentName');
    const studentId = getCookie('studentId');
    const studentNo = getCookie('studentNo');
    return !!studentName && !!studentId && !!studentNo;
};

// 路由守卫
router.beforeEach(async (to, _from, next) => {
    // 检查是否已登录
    const loggedIn = isUserLoggedIn();
    
    // 如果未登录且访问的不是登录页面，重定向到登录页面
    if (!loggedIn && to.path !== '/login') {
        next('/login');
        return;
    }
    
    // 如果已登录且访问登录页面，重定向到首页
    if (loggedIn && to.path === '/login') {
        next('/home');
        return;
    }
    
    // 未登录用户访问登录页面，直接放行
    if (!loggedIn && to.path === '/login') {
        next();
        return;
    }

    // 只有已登录用户才需要获取角色信息
    if (loggedIn) {
        // 直接读取 cookie 中的 role
        const roleStr = getCookie('role');
        const userRole = roleStr ? Number(roleStr) : null;

        // 判断是否访问教师端
        if (to.path.startsWith('/teacher')) {
            if (userRole === 200 || userRole === 300) {
                next();
                return;
            }
            else {
                ElMessage.warning('您没有教师权限，无法访问教师端');
                next('/home');
                return;
            }
        }

        // 非教师端页面 → 全部放行
        next();
        return;
    }
    
    // 其他情况默认放行
    next();
});

export default router;