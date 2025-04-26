import { request } from "./axios";
import { EventSourceMessage, fetchEventSource } from "@microsoft/fetch-event-source";

class DifyApi {
    static async upload(params: {} | undefined) {
        return request('/api/dify/upload', params, 'post')
    }

    static async chat(params: {} | undefined, callback: (event: EventSourceMessage) => void) {
        let ctrl = new AbortController();
        fetchEventSource('/api/dify/chat/stream', {
            method: 'POST',
            headers: { "content-type": 'application/json' },
            // openWhenHidden设置为true，意味着即使页面不可见，连接也会保持打开
            openWhenHidden: true,
            // body是请求的正文，是一个JSON字符串，包含后端接口需要的参数。
            body: JSON.stringify(params),
            signal: ctrl.signal,
            // 监听后端接口流式响应结果，追加并输出到页面上。
            onmessage(event) {
                // let obj = JSON.parse(event.data);
                // if(obj.event_type === "MESSAGE_END")
                //     return;
                callback(event);
            },
            // 当连接关闭时，触发onclose。
            onclose() { ctrl.abort(); },
            // 当请求发生异常时，触发onerror
            onerror(_val: any) { ctrl.abort(); },
        })
    }
}

export { DifyApi }