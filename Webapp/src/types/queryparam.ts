export interface QueryParam<T> {
    condition: T
}

export function createQueryParam<T>(defaultCondition: T): QueryParam<T> {
    return {
        condition: defaultCondition
    };
}