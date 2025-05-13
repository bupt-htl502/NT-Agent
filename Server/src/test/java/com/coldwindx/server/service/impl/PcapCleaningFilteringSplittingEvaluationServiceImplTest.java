package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import jakarta.annotation.Resource;
import org.checkerframework.checker.units.qual.C;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PcapCleaningFilteringSplittingEvaluationServiceImplTest {
    @Resource(name = "pcapCleaningFilteringSplittingEvaluationServiceImpl")
    private EffectEvaluationService service;

    @Test
    void testReadPcap() throws Exception {
        Commit commit = new Commit();
        commit.setStudentId(10001L);
        commit.setSceneId(2);
        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(10001L);
        student2Resource.setSceneId(2);
//        double score = service.evaluate(student2Resource, commit);
//        System.out.println(STR."score: \{score}");
    }
}
