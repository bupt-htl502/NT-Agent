/**
 * @description 设置后端服务
 */
import { request } from './axios'

class TeacherApi {
    static async query(params: {} | undefined) {
        return request('/api/transcript/getScript', params, 'get')
    }
}

export { TeacherApi }