<template>
    <div class="div-setting">
        <div class="div-condition">
            <span>键：</span>
            <el-select v-model="params.condition.key" filterable placeholder="Select" style="width: 240px">
                <el-option v-for="item in options" :value="item" />
            </el-select>
            <el-button type="primary" @click="query()">查询</el-button>
            <el-button type="success" @click="openInsertForm()">新增</el-button>
        </div>
        <el-table class="table" :data="data">
            <el-table-column prop="key" label="键" />
            <el-table-column prop="value" label="值" />
            <el-table-column prop="description" label="描述" />
            <el-table-column label="操作">
                <template #default="scope">
                    <el-button size="small" @click="openEditForm(scope.$index, scope.row)">
                        Edit
                    </el-button>
                    <el-button size="small" type="danger" @click="remove(scope.$index, scope.row)">
                        Delete
                    </el-button>
                </template>
            </el-table-column>
        </el-table>
        <div class="div-pagination">
            <el-pagination v-model:current-page="pagination.page" v-model:page-size="pagination.pagesize"
                :page-sizes="[20, 50, 100, 200]" layout="total, sizes, prev, pager, next, jumper" :total="pagination.total"
                @size-change="changePageSize" @current-change="changeCurrentPage" />
        </div>
    </div>


    <InsertForm v-model:visible="insert_form_visible" @after-submit="reload()"></InsertForm>
    <EditForm v-model:visible="edit_form_visible" :row="row_in_current_edit" @after-submit="reload()"></EditForm>
</template>
<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { Setting, createSetting } from '@/types/setting';
import { QueryParam, createQueryParam } from '@/types/queryparam';
import { Pagination, createPagination } from '@/types/pagination';
import { SettingApi } from '@/apis/SettingApi';
import InsertForm from './InsertForm.vue';
import EditForm from './EditForm.vue';

const options = ref<String[]>([])
const params = ref<QueryParam<Setting>>(createQueryParam<Setting>(createSetting()))
const data = ref<Setting[]>([])
const pagination = ref<Pagination>(createPagination())

const insert_form_visible = ref(false);
const edit_form_visible = ref(false);
const row_in_current_edit = ref<Setting>({} as Setting)
// 页面挂载
onMounted(() => { reload() })

const reload = () => {
    // 重置分页条件，但保留查询条件
    pagination.value = createPagination()
    params.value.limit = pagination.value.pagesize
    params.value.offset = (pagination.value.page - 1) * pagination.value.pagesize

    SettingApi.keys().then((res) => { options.value = res as String[] })
    SettingApi.count(params.value).then((res) => { pagination.value.total = res as number })
    SettingApi.query(params.value).then((res) => { data.value = res as Setting[] })
}

// 查询
const query = () => {
    params.value.limit = pagination.value.pagesize
    params.value.offset = (pagination.value.page - 1) * pagination.value.pagesize
    SettingApi.query(params.value).then((res) => { data.value = res as Setting[] })
}

// 新增页面
const openInsertForm = () => {
    insert_form_visible.value = true;
}

// 编辑页面
const openEditForm = (_index: number, row: Setting) => {
    row_in_current_edit.value = row;
    edit_form_visible.value = true;
}

// 删除功能
const remove = (_index: number, row: Setting) => {
    SettingApi.delete(row).then(() => { reload() })
}

const changePageSize = (val: number) => {
    pagination.value.pagesize = val
    reload()
}
const changeCurrentPage = (val: number) => {
    pagination.value.page = val
    query()
}
</script>

<style>
.div-setting {
    width: 100%;
    height: 100%;
    display: flex;
    flex-direction: column;
    /* background-color: aquamarine; */
}

.div-condition {
    width: 100%;
    display: flex;
    justify-content: center;
    gap: 10px;
    /* background-color: aquamarine; */
}

.table {
    width: 100%;
    height: 250;
    /* background-color: aqua; */
}

.div-pagination {
    width: 100%;
    display: flex;
    justify-content: center;
    margin-top: auto;
}
</style>