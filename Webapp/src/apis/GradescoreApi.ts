/**
 * @description 设置后端服务
 */
import { request } from './axios'

class getScoreApi {
    static async query( currentClassId: number ) {
        return request('/api/commit/getScoreList', { classId: currentClassId }, 'post')
    }
}

export { getScoreApi }