/**
 * @description 注册学生信息
 */
import { request } from './axios'

class StudentApi {
    static async insert(params: {} | undefined) {
        return request('/api/student/insert', params, 'post')
    }
    static async testModeInsert(params: {} | undefined) {
        return request('/api/student/testModeInsert', params, 'post')
    }
}

export { StudentApi }