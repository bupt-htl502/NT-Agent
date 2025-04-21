<template>
    <div class="div-tree-container">
        <el-tree-v2 class="tree" :data="data" :props="props" :height="800" />
    </div>
</template>

<script lang="ts" setup>
interface Tree {
    id: string
    label: string
    children?: Tree[]
}

const getKey = (prefix: string, id: number) => {
    return `${prefix}-${id}`
}

const createData = (
    maxDeep: number,
    maxChildren: number,
    minNodesNumber: number,
    deep = 1,
    key = 'node'
): Tree[] => {
    let id = 0
    return Array.from({ length: minNodesNumber })
        .fill(deep)
        .map(() => {
            const childrenNumber =
                deep === maxDeep ? 0 : Math.round(Math.random() * maxChildren)
            const nodeKey = getKey(key, ++id)
            return {
                id: nodeKey,
                label: nodeKey,
                children: childrenNumber
                    ? createData(maxDeep, maxChildren, childrenNumber, deep + 1, nodeKey)
                    : undefined,
            }
        })
}

const props = {
    value: 'id',
    label: 'label',
    children: 'children',
}
const data = createData(4, 30, 40)
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