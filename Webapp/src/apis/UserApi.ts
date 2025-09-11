/**
 * @description 学生老师身份识别
 */
import { request } from './axios'

class UserApi {
    static async getRole( params: {} | undefined ): Promise<"student" | "teacher"> {
        const response = await request("/api/auth/role", params, "get");
        const role = (response as any).role;
        return role;
    }
}

export { UserApi }