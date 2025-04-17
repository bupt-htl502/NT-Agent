package com.coldwindx.server.manager;

import io.minio.messages.Item;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.InputStream;
import java.util.List;

@SpringBootTest
class MinioManangerTest {
    @Autowired
    private MinioMananger mananger;
    @Test
    void getObject() throws Exception {
        InputStream obj = mananger.getObject("datasets", "experiment/1ERLiSkGzlE-2.pcap");
        System.out.println(obj);
    }

    @Test
    void getAllObjectsByPrefix() throws Exception {
        List<Item> objs = mananger.getAllObjectsByPrefix("datasets", "", true);
        List<String> names = objs.stream().map(item->item.objectName()).toList();
        System.out.println(names);
    }
}