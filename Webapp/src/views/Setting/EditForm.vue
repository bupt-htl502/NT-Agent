<template>
    <el-dialog v-model="visible" title="编辑" width="500" :before-close="close">
        <div class="div-form">
            <el-form :model="row" label-width="auto" style="max-width: 600px">
                <el-form-item label="键"><el-input v-model="row.key" /></el-form-item>
                <el-form-item label="值"><el-input v-model="row.value" type="textarea"/></el-form-item>
                <el-form-item label="描述"><el-input v-model="row.description" /></el-form-item>
            </el-form>
        </div>
        <template #footer>
            <div class="dialog-footer">
                <el-button @click="visible = false">取消</el-button>
                <el-button type="primary" @click="submit()">确认</el-button>
            </div>
        </template>
    </el-dialog>
</template>

<script setup lang="ts">
import { defineModel, defineProps, defineEmits } from 'vue';
import { SettingApi } from '@/apis/SettingApi';

const visible = defineModel<boolean>("visible")
const { row } = defineProps(['row'])
const emit = defineEmits(["after-submit"])

const close = (done: () => void) => { done() }
const submit = () => {
    SettingApi.update(row)
        .then(() => { emit("after-submit") })
        .then((_res) => { visible.value = false })
}
</script>