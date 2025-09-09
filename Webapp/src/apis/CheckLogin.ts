// 工具函数：从 Cookie 中获取指定键的值
const getCookie = (key: string): string | null => {
    const cookieArr = document.cookie.split('; ');
    for (const cookie of cookieArr) {
        const [name, value] = cookie.split('=');
        if (name === key) {
            return decodeURIComponent(value); // 解码（后端存的时候可能用了 URLEncoder）
        }
    }
    return null;
};

// 工具函数：判断是否已登录（前端层面）
export const isLogin = (): boolean => {
    // 关键：检查后端设置的登录标识 Cookie（必须和后端成功处理器中设置的一致）
    const userName = getCookie('userName'); // 对应后端 setCookie("userName", ...)
    const studentId = getCookie('studentId'); // 若后端设置了 studentId，也可加入判断
    const studentNo = getCookie('studentNo');
    // 只要核心标识存在，前端就认为“已登录”
    return !!userName && !!studentId&& !! studentNo;
};