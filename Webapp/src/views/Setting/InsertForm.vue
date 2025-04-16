<template>
    <el-dialog v-model="visible" title="新增" width="500" :before-close="close">
        <div class="div-form">
            <el-form :model="form" label-width="auto" style="max-width: 600px">
                <el-form-item label="键"><el-input v-model="form.key" /></el-form-item>
                <el-form-item label="值"><el-input v-model="form.value" /></el-form-item>
                <el-form-item label="描述"><el-input v-model="form.description" /></el-form-item>
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
import { ref, defineModel, defineEmits } from 'vue';
import { Setting } from '@/types/setting';
import { SettingApi } from '@/apis/SettingApi';

let visible = defineModel<boolean>("visible")
const form = ref<Setting>({} as Setting)
const emit = defineEmits(['after-submit']);

const close = (done: () => void) => { done() }
const submit = () => {
    SettingApi.insert(form.value)
        .then(() => { emit("after-submit") })
        .then(() => { visible.value = false })
}
</script>