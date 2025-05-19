package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.utils.EvaluateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MultiCSVStringFeaturesEvaluationServiceImplTest {
    @Autowired
    private MultiCSVStringFeaturesEvaluationServiceImpl impl;
    @Autowired
    private MinioMananger minioMananger;

    @Test
    void LoadZipMD5Test() throws Exception {
        String a = "temporary";
        String b = "/group_000.zip";
        InputStream zipInputStream = minioMananger.getObject(a,b);
        Map<String, Object> mp =  impl.readCsvZip(zipInputStream);
        System.out.println("ok");
    }
    @Test
    void TestCompare() throws Exception {

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("datasets/answer/40002/group_000.csv");
        Commit commit = new Commit();
        commit.setPath("temporary/group_000_error2.zip");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);
        CommitVO commitVO = impl.compare(results,standards);
        System.out.println("ok");
    }
}
