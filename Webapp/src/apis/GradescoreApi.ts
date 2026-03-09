/**
 * @description 设置后端服务
 */
import { request } from './axios'

class getScoreApi {
    static async query() {
        return request('/api/commit/getScoreList', {}, 'get')
    }
}

export { getScoreApi }