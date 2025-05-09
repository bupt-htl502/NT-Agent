/**
 * @description 注册学生信息
 */
import { request } from './axios'

class StudentApi {
    static async insert(params: {} | undefined) {
        return request('/api/student/insert', params, 'post')
    }
}

export { StudentApi }