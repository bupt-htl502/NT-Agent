/**
 * @description 智能体对话记忆后端服务
 */
import { request } from './axios'

export class MemoryApi {
    static async insert(params: {} | undefined) {
        return request('/api/memory/insert', params, 'post')
    }
}