package com.coldwindx.server.manager;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;

@SpringBootTest
class MinioManangerTest {
    @Autowired
    private MinioMananger mananger;
    @Test
    void getObject() throws Exception {
        InputStream obj = mananger.getObject("datasets", "experiment/1ERLiSkGzlE-2.pcap");
        System.out.println(obj);
    }
}