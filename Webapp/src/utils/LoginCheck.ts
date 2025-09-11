const getCookie = (key: string): string | null => {
    const cookieArr = document.cookie.split('; ');
    for (const cookie of cookieArr) {
        const [name, value] = cookie.split('=');
        if (name === key) {
            return decodeURIComponent(value);
        }
    }
    return null;
};

const isLogin = (): boolean => {
    const studentName = getCookie('studentName');
    const studentId = getCookie('studentId');
    const studentNo = getCookie('studentNo');
    return !!studentName && !!studentId&& !! studentNo;
};

export {isLogin}