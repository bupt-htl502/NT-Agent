/**
 * @description 学生班级接口
 */
interface RespResult<T = any> {
  code: number;
  message: string;
  data: T;
}

interface ClassRequest {
  className: string;
  studentNo: string | null;
  studentName: string | null;
}

interface joinClassRequest {
  studentNo: string | null;
  classCode: string | null;
}

interface quitClassRequest {
  studentNo: string | null;
}

interface queryClassRequest {
  teacherNo: string | null;
}

import { request } from './axios'

class ClassApi {
    static async query( params: queryClassRequest ): Promise<{ classes: string; message: string }> {
        const response = await request("/api/class/query", params, "post");
        return response as { classes: string; message: string };
    }
    static async create( params: ClassRequest ): Promise<RespResult<string>> {
        const response = await request("/api/class/create",params, "post");
        return response as RespResult<string>;
    }
    static async join( params: joinClassRequest ): Promise<RespResult<string>> {
        const response = await request("/api/class/join",params, "post");
        return response as RespResult<string>;
    }
    static async quit( params: quitClassRequest ): Promise<RespResult<string>> {
        const response = await request("/api/class/quit",params, "post");
        return response as RespResult<string>;
    }
}

export { ClassApi }