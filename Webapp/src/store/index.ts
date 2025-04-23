/*
* @description 系统配置
*/
import { defineStore } from "pinia";

const useStore = defineStore('main', {
    state: ()=>({
        agent_end_point: "http://10.101.170.78/chatbot/EArf8URSfhCXm5lL"
    })
})

export default useStore