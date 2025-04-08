<template>
    <div class="div-agent" style="height: 100%;">
        <div style="width: 15%; height: 100%; background-color: aqua;">
            <p>History</p>
        </div>
        <div class="div-dialog">
            <AgentDialog/>
        </div>
    </div>
</template>

<script setup lang="ts">
import AgentDialog from './AgentDialog.vue'
import AgentIframe from './AgentIframe.vue';
</script>

<style scoped>
.div-agent{
    display: flex;
    align-items: center; 
}

.div-dialog{
    width: 85%; 
    height: 100%; 
    flex-direction:column; 
    display: flex;
    align-items: center;
    border-radius: 8px;
    border-style: solid;
    border-color: blue;
    border-width: 1px;
    /* background-color: brown; */
}
</style>
