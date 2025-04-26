import { request } from "./axios";
import { EventSourceMessage, fetchEventSource } from "@microsoft/fetch-event-source";
class DifyApi {
    static async upload(params: {} | undefined) {
        return request('/api/dify/upload', params, 'post')
    }

    static async chat(params: {} | undefined, 
        callback: (event: EventSourceMessage) => void,
        errorback: (err: any) => void) {
        
        fetchEventSource('/api/dify/chat/stream', {
            method: 'POST',
            headers: { "content-type": 'application/json' },
            // openWhenHidden设置为true，意味着即使页面不可见，连接也会保持打开
            openWhenHidden: true,
            // body是请求的正文，是一个JSON字符串，包含后端接口需要的参数。
            body: JSON.stringify(params),
            // 监听后端接口流式响应结果，追加并输出到页面上。
            onmessage(event) {
                // let obj = JSON.parse(event.data);
                // console.log(obj)
                // if(obj.event_type === "MESSAGE_END")
                //     return;
                callback(event);
            },
            // 当连接关闭时，触发onclose。
            onclose() { 
                console.log('Connection closed by the server');
                // ctrl.abort();
            },
            // 当请求发生异常时，触发onerror，禁止接口重试
            // https://juejin.cn/post/7411047482652262415
            onerror(err) { 
                errorback(err);
                throw err;
            },
        })
    }
}

export { DifyApi }