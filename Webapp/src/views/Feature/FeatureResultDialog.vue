<template>
    <div class="dialog-container">
        <div class="dialog-content">
            <el-text class="mx-1 indented" size="large" v-loading="loading" element-loading-text="Loading...">{{ answer
            }}</el-text>
        </div>
        <div class="dialog-tip">
            <label class="dialog-label">不懂怎么做</label>
            <el-button style="border-style: none;" @click="visible = true">
                <svg t="1745548222326" class="icon" viewBox="0 0 1024 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
                    p-id="17760" width="16" height="16">
                    <path
                        d="M463.99957 784.352211c0 26.509985 21.490445 48.00043 48.00043 48.00043s48.00043-21.490445 48.00043-48.00043c0-26.509985-21.490445-48.00043-48.00043-48.00043S463.99957 757.842226 463.99957 784.352211z"
                        fill="#1296db" p-id="17761"></path>
                    <path
                        d="M512 960c-247.039484 0-448-200.960516-448-448S264.960516 64 512 64 960 264.960516 960 512 759.039484 960 512 960zM512 128.287273c-211.584464 0-383.712727 172.128262-383.712727 383.712727 0 211.551781 172.128262 383.712727 383.712727 383.712727 211.551781 0 383.712727-172.159226 383.712727-383.712727C895.712727 300.415536 723.551781 128.287273 512 128.287273z"
                        fill="#1296db" p-id="17762"></path>
                    <path
                        d="M512 673.695256c-17.664722 0-32.00086-14.336138-32.00086-31.99914l0-54.112297c0-52.352533 39.999785-92.352318 75.32751-127.647359 25.887273-25.919957 52.67249-52.67249 52.67249-74.016718 0-53.343368-43.07206-96.735385-95.99914-96.735385-53.823303 0-95.99914 41.535923-95.99914 94.559333 0 17.664722-14.336138 31.99914-32.00086 31.99914s-32.00086-14.336138-32.00086-31.99914c0-87.423948 71.775299-158.559333 160.00086-158.559333s160.00086 72.095256 160.00086 160.735385c0 47.904099-36.32028 84.191695-71.424378 119.295794-27.839699 27.776052-56.575622 56.511974-56.575622 82.3356l0 54.112297C544.00086 659.328155 529.664722 673.695256 512 673.695256z"
                        fill="#1296db" p-id="17763"></path>
                </svg>
            </el-button>
        </div>
    </div>

    <el-dialog v-model="visible" append-to-body>
        <template #header="{ titleId, titleClass }">
            <h4 :id="titleId" :class="titleClass">特征提取详细步骤</h4>
        </template>
        <FeatureHintDialog></FeatureHintDialog>
    </el-dialog>
</template>

<script lang="ts" setup>
import { ref, defineProps, onMounted } from 'vue';
import { DifyApi } from "@/apis/DifyApi";
import FeatureHintDialog from "@/views/Feature/FeatureHintDialog.vue";

const { fileid, feature } = defineProps(['fileid', "feature"]);
const visible = ref<boolean>(false);
const loading = ref<boolean>(true);
const answer = ref<String>("")
onMounted(() => {
    DifyApi.chat({ "fileid": fileid, "query": "请对这个pcap文件提取" + feature.name + "特征，并解释该特征的含义。" })
        .then((res: any) => {
            answer.value = res.answer;
            loading.value = false;
        })
});
</script>

<style lang="scss" scoped>
.dialog-container {
    display: flex;
    flex-direction: column;
    width: 100%;
    height: 100%;
}

.dialog-content {
    margin-left: 3%;
    margin-right: 3%;
    min-height: 100px;
    // background-color: aqua;
}

.indented {
    display: block;
    text-indent: 2em;
}

.dialog-label {
    font-size: 16px;
    color: gray;
}

.dialog-tip {
    display: flex;
    align-items: center;
    flex-direction: row;
    margin-top: 1%;
    margin-left: auto;
    margin-right: 3%;
}
</style>