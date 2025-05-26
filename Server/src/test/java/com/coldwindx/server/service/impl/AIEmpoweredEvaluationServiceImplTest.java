package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AIEmpoweredEvaluationServiceImplTest {
    @Autowired
    private AIEmpoweredEvaluationServiceImpl impl;
    @Test
    void testCompare() throws Exception {

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("temporary/AIempower/output.csv");
        Commit commit = new Commit();
        commit.setPath("temporary/AIempower/result_error_5.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);
        CommitVO commitVO = impl.compare(results, standards);
        System.out.println("pause");
    }
}
