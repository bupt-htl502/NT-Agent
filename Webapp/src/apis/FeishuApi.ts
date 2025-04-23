/**
 * @description 飞书服务后端接口
 */
import { request } from './axios'

class FeishuApi {
    static async signature(params: {} | undefined) {
        return request('/api/feishu/signature', params, 'post')
    }
}

export { FeishuApi }