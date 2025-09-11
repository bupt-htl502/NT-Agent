/**
 * @description 学生老师身份识别
 */
import { request } from './axios'

class UserApi {
    static async getRole(): Promise<"student" | "teacher"> {
        const response = await request("/api/auth/role", {}, "get");
        const role = (response as any).role;
        return role;
    }
}

export { UserApi }