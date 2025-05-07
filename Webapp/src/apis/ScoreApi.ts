/**
 * @description 评估分数后端服务
 */
import { request } from './axios'

class ScoreApi {
    static async insert(params: {} | undefined) {
        return request('/api/commit/insert', params, 'post')
    }
}

export { ScoreApi }