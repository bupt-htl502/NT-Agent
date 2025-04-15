/**
 * @description 封装请求配置拦截器
 */
import axios from 'axios';
import { showMessage } from "./status";   // 引入状态码文件
import { ElMessage } from 'element-plus'  // 引入el 提示框，这个项目里用什么组件库这里引什么

// 设置接口超时时间
axios.defaults.timeout = 60000;

// axios.defaults.baseURL = import.meta.env.VITE_API_URL
// console.log(axios.defaults.baseURL)
//http request 拦截器
axios.interceptors.request.use(
    config => {
        // TO-DO: 发送请求之前的任务
        return config;
    },
    error => { return Promise.reject(error); }
);

//http response 拦截器
axios.interceptors.response.use(
    response => {
        const { code, message, data } = response.data;
        // 根据后端返回的自定义状态码 code 进行错误信息提示（根据具体需求确定是否需要书写）
        switch (code) {
            case 0:
                return Promise.resolve(data);
            case 1001:
                ElMessage({ message: '登录信息已过期，请重新登录！', type: 'error' })
                return Promise.reject(message);
            case 1002:
                ElMessage({ message: '当前账号已在其它端登录，请重试！', type: 'error' })
                return Promise.reject(message);
            case 1003:
                ElMessage({ message: message || '未知错误', type: 'error' })
                return Promise.reject(message);
            default:
                ElMessage({ message: '服务器内部错误！' + message, type: 'error' })
                return Promise.reject(message);
        }
    },
    error => {
        const { response } = error;
        if (response) {
            // 请求已发出，但是不在2xx的范围
            showMessage(response.status);           // 传入响应码，匹配响应码对应信息
            return Promise.reject(response.data);
        } else {
            ElMessage.warning('网络连接异常,请稍后再试!');
        }
    }
);

// 封装 GET POST 请求并导出
export function request(url = '', params = {}, method = 'POST') {
    //设置 url params type 的默认值
    return new Promise((resolve, reject) => {
        let promise
        if (method.toUpperCase() === 'GET') {
            promise = axios({ url, params })
            promise.then(res => { resolve(res) }).catch(err => { reject(err) })
        }
        if (method.toUpperCase() === 'POST') {
            promise = axios({ method: 'POST', url, data: params })
            promise.then(res => { resolve(res)}).catch(err => { reject(err) })
        }
    })
}