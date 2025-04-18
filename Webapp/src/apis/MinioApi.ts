/**
 * @description 设置后端服务
 */
import { request } from './axios'

class MinioApi {
    static async listBucketNames() {
        return request('/api/minio/listBucketNames', {}, 'post')
    }
    static async download(params: {} | undefined){
        return request('/api/minio/download', params, 'download')
    }
}

export{
    MinioApi
}