package com.coldwindx.server.manager;

import com.coldwindx.server.entity.form.FeishuWebComponentAuth;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class FeishuManagerTest {
    @Autowired
    private FeishuManager manager;
    @Test
    void signature() {
        FeishuWebComponentAuth obj = manager.signature("");
        System.out.println(obj);
    }
}