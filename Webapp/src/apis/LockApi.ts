/**
 * @description 设置后端服务
 */
import { request } from './axios'

class LockApi {
    static async query(params: {} | undefined) {
        return request('/api/lock/query', params, 'post')
    }
}

export { LockApi }