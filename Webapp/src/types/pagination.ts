export interface Pagination {
    page: number
    pagesize: number
    total: number
}

export function createPagination(): Pagination {
    return {
        page: 1,
        pagesize: 20,
        total: 0
    };
}