/*
* @description 系统配置
*/
import { ref } from 'vue'
import { defineStore } from 'pinia'

const useDifyStore = defineStore('dify',
    () => {
        const agent_end_point = ref<string>("http://10.101.170.78/chatbot/EArf8URSfhCXm5lL")
        return { agent_end_point }
    },
    {
        persist: {
            key:'agent_end_point',
            storage:localStorage
        }
    }
)

export default useDifyStore