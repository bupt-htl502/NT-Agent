<template>
  <div class="experiment-container">
    <span class="experiment-title">{{ props.title }}</span>
    <div class="experiment-body">
      <div class="experiment-qa">
        <iframe class="experiment-agent" :src="agent_end_point" frameborder="0" />
      </div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { storeToRefs } from "pinia";
import { useDifyStore } from "@/store/index";

const store = useDifyStore();
const { agent_end_point } = storeToRefs(store);
const props = defineProps(["title"])
</script>

<style lang="scss" scoped>
.experiment-container {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 90vh; /* 使用视口高度确保充满整个页面 */
}

.experiment-title {
  font-size: 20px;
  font-weight: bold;
  color: #333;
  margin: 0 0 10px 20px;
}

.experiment-body {
  display: flex;
  flex: 1; /* 占据剩余全部空间 */
}

.experiment-qa {
  width: 100%; /* 改为100%宽度 */
  height: 100%; /* 充满高度 */
}

.experiment-agent {
  width: 100%;
  height: 100%;
}
</style>