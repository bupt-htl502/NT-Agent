<template>
    <div class="div-condition">
        <span>键：</span>
        <el-select v-model="params.condition.key" filterable placeholder="Select" style="width: 240px">
            <el-option v-for="item in options" :value="item" />
        </el-select>
        <el-button class="button-insert" type="primary" @click="query()">查询</el-button>
        <el-button class="button-insert" type="success" @click="openInsertForm()">新增</el-button>
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

    <InsertForm v-model:visible="insert_form_visible" @after-submit="query()"></InsertForm>
    <EditForm v-model:visible="edit_form_visible" :row="row_in_current_edit" @after-submit="query()"></EditForm>
</template>
<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { Setting, createSetting } from '@/types/setting';
import { QueryParam, createQueryParam } from '@/types/queryparam';
import { SettingApi } from '@/apis/SettingApi';
import InsertForm from './InsertForm.vue';
import EditForm from './EditForm.vue';

const options = ref<String[]>([])
const params = ref<QueryParam<Setting>>(createQueryParam<Setting>(createSetting()))
const data = ref<Setting[]>([])

const insert_form_visible = ref(false);
const edit_form_visible = ref(false);
const row_in_current_edit = ref<Setting>({} as Setting)
// 页面挂载
onMounted(() => { query() })

// 查询
const query = () => {
    SettingApi.keys().then((res) => { options.value = res as String[] })
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
    SettingApi.delete(row).then(() => { query() })
}
</script>

<style>
.div-condition {
    width: 80%;
    display: flex;
    /* background-color: aquamarine; */
}

.button-insert {
    margin-left: auto;
}

.table {
    width: 100%;
    height: 250;
    /* background-color: aqua; */
}
</style>