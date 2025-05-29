<template>
    <div class="div-tree-container">
        <el-tree class="tree"
                 :data="data"
                 :height="800"
                 node-key="id"
                 :default-expanded-keys="expandedKeys"
                 @node-click="onclick">
            <template #default="{ data }">
                <!-- 自定义节点内容 -->
                <span :class="setNodeClass(data)">
                    {{ data.label }}
                </span>
            </template>
        </el-tree>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import { SettingApi } from "@/apis/SettingApi";

interface TreeNode {
    id: number;
    label: string;
    parent: number;
    level: number;
    url: string;
    children?: TreeNode[];
}
const data = ref<TreeNode[]>([])
const expandedKeys = ref<number[]>([]);

onMounted(() => {
    SettingApi.query({ "condition": { "key": "VUE_CONTENT_NODE" } }).then((res: any) => {
        let treenodes = res.map(item => JSON.parse(item.value) as TreeNode)
        treenodes.sort((a, b) => a.level - b.level);

        let hashnodes = ref<Record<number, TreeNode>>({})
        treenodes.forEach(item => {
            hashnodes.value[item.id] = item
            if (item.level === 1) {
              expandedKeys.value.push(item.id);
            }
            if (item.parent == 0) {
                data.value.push(item)
                return
            }

            let parent = hashnodes.value[item.parent]
            if (parent.children == undefined){
              parent.children = [];
            }
            parent.children.push(item)
        });
    });
});

const router = useRouter()
const onclick = (node: TreeNode) => {
    if (node.url == undefined || node.url == "")
        return

    router.push({ name: node.url, query: { title: node.label } })
}

const setNodeClass = (data) => data.parent == 0 ? "node-root" : "node-child"
</script>

<style lang="scss" scoped>
.div-tree-container {
    width: 100%;
    height: 100%;
    // background-color: black;
}

.tree {
    height: 135%;
    width: 120%;
    overflow: hidden;
    // background-color: black;
}

.node-root {
    font-size: 18px;
    font-weight: bold;
    color: black;
}

.node-child {
    font-size: 16px;
    color: black;
    cursor: pointer;
}
</style>