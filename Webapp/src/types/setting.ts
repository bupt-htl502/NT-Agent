export interface Setting {
    id: number
    key: string
    value: string
    description: string
}
/**
 * 使用工厂函数赋默认值
 * @param init 
 * @returns 
 */
export function createSetting(init?: Partial<Setting>): Setting {
    return {
        id: 0,
        key: "",
        value: '',
        description: '',
        ...init
    };
}