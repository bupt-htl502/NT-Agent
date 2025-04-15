<template>
    <div class="div-condition">
        <el-button class="button-insert" type="success" @click="openInsertForm()">新增</el-button>
    </div>
    <el-table class="table" :data="data">
        <el-table-column prop="key" label="键" />
        <el-table-column prop="value" label="值" />
        <el-table-column prop="description" label="描述" />
        <el-table-column label="操作">
            <template #default="scope">
                <el-button size="small" @click="handleEdit(scope.$index, scope.row)">
                    Edit
                </el-button>
                <el-button size="small" type="danger" @click="handleDelete(scope.$index, scope.row)">
                    Delete
                </el-button>
            </template>
        </el-table-column>
    </el-table>

    <InsertForm v-model:visible="insert_form_visible"></InsertForm>
</template>
<script lang="ts" setup>
import { onMounted, ref } from 'vue';
import { Setting } from '@/types/setting';
import {SettingApi} from '@/apis/SettingApi';
import InsertForm from './InsertForm.vue';

const data = ref<Setting[]>([])
const insert_form_visible = ref(false);
// 页面挂载
onMounted(()=>{
    SettingApi.query({}).then((res)=>{data.value = res as Setting[]})
})
const handleEdit = (index: number, row: Setting) => {
    console.log(index, row)
}
const handleDelete = (index: number, row: Setting) => {
    console.log(index, row)
}

// 新增页面
const openInsertForm = ()=>{
    insert_form_visible.value = true;
}
</script>

<style>
.div-condition{
    width: 80%;
    display: flex;
    background-color: aquamarine;
}
.button-insert{
    margin-left: auto;
}
.table {
    width: 100%;
    height: 250;
    /* background-color: aqua; */
}
</style>