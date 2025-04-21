<template>
    <div class="div-brick">
        <label class="brick-label">{{ props.name }}</label>
        <div class="brick-img">{{ props.image }}</div>
        <label class="brick-description">{{ props.description }}</label>
        <el-button class="brick-button" @click="onclick(props.route)">进入实验</el-button>
    </div>
</template>

<script setup lang="ts">
import { defineProps } from 'vue';
import { useRouter } from 'vue-router';

const router = useRouter();
const props = defineProps({
    "name": {
        type: String,
        required: true
    },
    "image": {
        type: String,
        required: true
    },
    "description": {
        type: String,
        required: true
    },
    "route": {
        type: String,
        required: true
    }
});

const onclick = (route: string) => {
    console.log("onclick", route);
    router.push({ name: route });
};

</script>

<style scoped>
.div-brick {
    width: 100%;
    height: 360px;
    display: flex;
    flex-direction: column;


}

.div-brick:hover {
    border-style: solid;
    border-color: blue;
    border-width: 1px;
    border-radius: 2%;
}
.brick-label {
    display: flex;
    align-items: center;
    font-size: 18px;
    font-weight: bold;
    margin-left: 10px;
    margin-right: 10px;
    margin-top: 10px;
}

.brick-img {
    height: 50%;

    margin-left: 10px;
    margin-right: 10px;

    border-style: solid;
    border-radius: 2%;
    border-width: 1px;
    border-color: blue;
}

.brick-description {
    display: flex;
    align-items: center;
    font-size: 16px;
    margin-left: 10px;
    margin-right: 10px;
    margin-top: 10px;
}

.brick-button {
    display: flex;
    align-items: center;
    justify-content: center;
    margin-left: 10px;
    margin-right: 10px;
    margin-top: auto;
    margin-bottom: 10px;

    color: white;
    background-color: #2563eb;
}
</style>