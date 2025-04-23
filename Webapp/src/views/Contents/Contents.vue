<template>
    <div class="div-tree-container">
        <el-tree class="tree" :data="data" :height="800" @node-click="onclick">
            <template #default="{ node, data }">
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
import type Node from 'element-plus/es/components/tree/src/model/node'
import type { TreeNodeData } from 'element-plus/es/components/tree/src/tree.type'
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

onMounted(() => {
    SettingApi.query({ "condition": { "key": "VUE_CONTENT_NODE" } }).then((res) => {
        let treenodes = res.map(item => JSON.parse(item.value) as TreeNode)
        treenodes.sort((a, b) => a.level - b.level);

        let hashnodes = ref<Record<number, TreeNode>>({})
        treenodes.forEach(item => {
            hashnodes.value[item.id] = item
            if (item.parent == 0) {
                data.value.push(item)
                return
            }

            let parent = hashnodes.value[item.parent]
            if (parent.children == undefined)
                parent.children = []
            parent.children.push(item)
        });
    })
})

// todo: 这里定义树节点点击后的跳转事件
const onclick = (node: TreeNode) => {
    console.log(node)
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
    height: 100%;
    overflow: hidden;
    // background-color: black;
}

.node-root {
    font-size: 18px;
    font-weight: bold;
    color: black;
}

.node-child{
    font-size: 16px;
    color: black;
    cursor: pointer;
}
</style>