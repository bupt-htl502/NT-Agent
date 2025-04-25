package com.coldwindx.server.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.ArrayList;

@SpringBootTest
class DifyManagerTest {
    @Autowired
    private DifyManager difyManager;

    @Test
    void chat() throws IOException {
        difyManager.chat("你是谁？", new ArrayList<>());
    }
}