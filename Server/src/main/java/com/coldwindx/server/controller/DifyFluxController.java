package com.coldwindx.server.controller;

import com.coldwindx.server.entity.NtAgentException;
import com.coldwindx.server.entity.enums.ResponseCode;
import com.coldwindx.server.service.DifyService;
import io.github.imfangs.dify.client.callback.ChatStreamCallback;
import io.github.imfangs.dify.client.event.ErrorEvent;
import io.github.imfangs.dify.client.event.MessageEndEvent;
import io.github.imfangs.dify.client.event.MessageEvent;
import io.github.imfangs.dify.client.util.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.FluxSink;
import reactor.core.scheduler.Schedulers;

import java.io.IOException;
import java.time.Duration;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping(value = "dify/flux")
public class DifyFluxController {
    @Autowired
    private DifyService service;

    @PostMapping(value = "/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<Map<String, Object>> sendChatFlowMessageStream(@RequestBody Map<String, String> params){
        String fileid = params.getOrDefault("fileid", "");
        String query = params.getOrDefault("query", "");
        if(StringUtils.isBlank(fileid))
            throw new NtAgentException(ResponseCode.INVALID_PARAMETERS.getCode(), "参数非法：`fileid`不能为空！");
        if(StringUtils.isBlank(query))
            throw new NtAgentException(ResponseCode.INVALID_PARAMETERS.getCode(), "参数非法：`query`不能为空！");
        return Flux.create((FluxSink<Map<String, Object>> emitter) -> {
                    // 注册回调
                    ChatStreamCallback callback = new ChatStreamCallback() {
                        @Override
                        public void onMessage(MessageEvent event) {
                            log.debug("收到消息片段: {}", event);
                            emitter.next(JsonUtils.jsonToMap(JsonUtils.toJson(event)));
                        }

                        @Override
                        public void onMessageEnd(MessageEndEvent event) {
                            emitter.next(JsonUtils.jsonToMap(JsonUtils.toJson(event)));
                            emitter.complete();
                        }

                        @Override
                        public void onError(ErrorEvent event) {
                            emitter.next(JsonUtils.jsonToMap(JsonUtils.toJson(event)));
                            emitter.complete();
                        }

                        @Override
                        public void onException(Throwable throwable) {
                            emitter.error(throwable);
                        }
                    };
                    log.debug("Flux.create!");
                    // 发起流式请求
                    try {
                        service.chat(query, fileid, callback);
                    } catch (IOException e) {
                        log.error("流式请求对话服务失败！{}", e.getMessage(), e);
                        emitter.error(new NtAgentException(ResponseCode.REMOTE_SERVICE_ERROR));
                    }
                })
                .subscribeOn(Schedulers.boundedElastic())   // 指定异步线程
                .timeout(Duration.ofSeconds(30));            // 超时控制
    }

}
