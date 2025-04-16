export interface QueryParam<T> {
    condition: T
    offset: number
    limit: number
}

export function createQueryParam<T>(defaultCondition: T): QueryParam<T> {
    return {
        condition: defaultCondition,
        offset: 0,
        limit: 20
    };
}