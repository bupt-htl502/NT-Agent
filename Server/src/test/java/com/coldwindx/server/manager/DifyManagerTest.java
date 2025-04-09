package com.coldwindx.server.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class DifyManagerTest {
    @Autowired
    private DifyManager difyManager;
    @Test
    void runWorkflow() {
        Map<String, Object> inputs = new HashMap<>();
        inputs.put("question", "请介绍一下人工智能的应用场景");
        difyManager.runWorkflow(inputs);
    }
}