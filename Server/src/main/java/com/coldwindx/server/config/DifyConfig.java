package com.coldwindx.server.config;

import io.github.imfangs.dify.client.DifyChatflowClient;
import io.github.imfangs.dify.client.DifyClientFactory;
import io.github.imfangs.dify.client.DifyWorkflowClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DifyConfig {
    @Value("${dify.url}")
    private String url;
    @Value("${dify.app-key}")
    private String appKey;

    @Bean
    public DifyWorkflowClient difyWorkflowClient(){
        // 创建工作流客户端
        return DifyClientFactory.createWorkflowClient(url, appKey);
    }

    @Bean
    public DifyChatflowClient difyChatflowClient(){
        return DifyClientFactory.createChatWorkflowClient(url, appKey);
    }
}
