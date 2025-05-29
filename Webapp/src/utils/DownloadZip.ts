import { ElLoading, ElMessage } from 'element-plus';
import {Student2ResourceApi} from "@/apis/Student2ResourceApi.ts";

class FormParam {
    id: number = 0;
    isdeleted: boolean = false;
}

class Student2Resource extends FormParam {
    studentId: string | number | null;
    sceneId: number;
    path: string;
    criterion: string;
    createTime: number;

    constructor(
        studentId: string | number | null,
        sceneId: number,
        path: string,
        criterion: string,
        createTime: number
    ) {
        super();
        this.studentId = studentId;
        this.sceneId = sceneId;
        this.path = path;
        this.criterion = criterion;
        this.createTime = createTime;
    }
}
class QueryParam<T> {
    condition: T;
    offset: number = 0;
    limit: number = -1;

    constructor(condition: T) {
        this.condition = condition;
    }
}

/**
 * 通用实验数据下载器
 * @param studentid 学生 ID
 * @param sceneid 场景 ID，用于查询资源
 */
export const downloadExperimentData = async (studentid: number, sceneid: number) => {
    const loading = ElLoading.service({
        lock: true,
        text: '正在下载中请稍后...',
        background: 'rgba(0, 0, 0, 0.7)',
    });

    try {
        const studentCondition = new Student2Resource(studentid, sceneid, '', '', Date.now());
        const student2resource = new QueryParam(studentCondition);
        const result = await Student2ResourceApi.query(student2resource) as Student2Resource[];
        const results2r = result[0];

        const segments = results2r.path.split('/');
        const bucketstr = segments[0];
        const pathstr = '/' + segments.slice(1).join('/');

        const originalFilename = pathstr.substring(pathstr.lastIndexOf('/') + 1);
        const ext = originalFilename.substring(originalFilename.lastIndexOf('.')); // 提取后缀名
        const filename = `experiment_data${ext}`;

        const downloadUrl = `/api/minio/download?bucket=${encodeURIComponent(bucketstr)}&path=${encodeURIComponent(pathstr)}`;

        // 创建下载链接并模拟点击
        const link = document.createElement('a');
        link.href = downloadUrl;
        link.setAttribute('download', filename);
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);

        ElMessage.success('下载已在后台启动');
    } catch (error) {
        console.error('查询文件信息失败:', error);
        ElMessage.error('无法获取文件信息，请重试！');
    } finally {
        loading.close();
    }
};
