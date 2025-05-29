/**
 * @description 根据学生id和实验场景id查分发给学生的作业路径
 */
import { request } from './axios'

class Student2ResourceApi {
    static async query(params: {} | undefined) {
        return request('/api/student2resource/query', params, 'post')
    }
}

export { Student2ResourceApi }