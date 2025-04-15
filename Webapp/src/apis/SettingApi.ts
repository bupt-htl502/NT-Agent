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
    static async update(params: {} | undefined) {
        return request('/api/setting/update', params, 'post')
    }
    static async delete(params: {} | undefined) {
        return request('/api/setting/delete', params, 'post')
    }
}

export{
    SettingApi
}