/**
 * @description 设置后端服务
 */
import { request } from './axios'

class SettingApi {
    static async query(params: {} | undefined) {
        return request('/api/setting/query', params, 'post')
    }
    static async insert(params: {} | undefined) {
        return request('/api/setting/insert', params, 'post')
    }
}

export{
    SettingApi
}