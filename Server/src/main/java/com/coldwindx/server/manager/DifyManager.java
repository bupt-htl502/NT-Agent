package com.coldwindx.server.manager;

import io.github.imfangs.dify.client.DifyWorkflowClient;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.model.workflow.WorkflowRunRequest;
import io.github.imfangs.dify.client.model.workflow.WorkflowRunResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Map;

@Component
public class DifyManager {
    @Autowired
    private DifyWorkflowClient client;

    public void runWorkflow(Map<String, Object> inputs){
        // 创建工作流请
        WorkflowRunRequest request = WorkflowRunRequest.builder()
                .inputs(inputs)
                .responseMode(ResponseMode.BLOCKING)
                .user("user-123")
                .build();

        // 执行工作流并获取响应
        WorkflowRunResponse response = null;
        try {
            response = client.runWorkflow(request);
            System.out.println("工作流执行ID: " + response.getTaskId());
            // 输出结果
            if (response.getData() != null) {
                for (Map.Entry<String, Object> entry : response.getData().getOutputs().entrySet()) {
                    System.out.println(entry.getKey() + ": " + entry.getValue());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
