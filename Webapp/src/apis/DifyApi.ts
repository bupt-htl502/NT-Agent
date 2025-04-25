import { request } from "./axios";

class DifyApi {
    static async upload(params: {} | undefined) {
        return request('/api/dify/upload', params, 'post')
    }
    static async chat(params: {} | undefined) {
        return request('/api/dify/chat', params, 'post')
    }
}

export { DifyApi }