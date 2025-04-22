<template>
    <div class="div-tree-container">
        <el-tree class="tree" :data="data" :props="props" :height="800" @node-click="onclick"/>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted } from 'vue';
import { SettingApi } from "@/apis/SettingApi";

interface TreeNode {
    id: number;
    label: string;
    parent: number;
    level: number;
    url: string;
    children?: TreeNode[];
}

const props = {
    value: 'id',
    label: 'label',
    children: 'children',
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
            if(parent.children == undefined)
                parent.children = []
            parent.children.push(item)
        });
    })
})

const onclick = (node: TreeNode) => {
    console.log(node)
}
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
</style>