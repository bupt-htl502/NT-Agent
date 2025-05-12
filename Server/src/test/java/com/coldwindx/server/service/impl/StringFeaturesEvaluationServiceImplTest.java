package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StringFeaturesEvaluationServiceImplTest {
    @Resource(name = "stringFeaturesEvaluationServiceImpl")
    private EffectEvaluationService service;
    @Test
    void loadFromCSV() throws Exception {
        System.out.println("loadFromCSV");
        StringFeaturesEvaluationServiceImpl impl = (StringFeaturesEvaluationServiceImpl) service;
        impl.loadFromCSV("temporary/resultTest.csv");
        System.out.println("loadFromCSV");
    }

    @Test
    void testBeforeCompare() throws Exception {
        StringFeaturesEvaluationServiceImpl impl = (StringFeaturesEvaluationServiceImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("temporary/answerTest.csv");
        Commit commit = new Commit();
        commit.setPath("temporary/resultTest.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);

        System.out.println("beforeCompare results size: " + standards.size());
        System.out.println("beforeCompare results size: " + results.size());
    }

    @Test
    void testCompare() throws Exception {
        StringFeaturesEvaluationServiceImpl impl = (StringFeaturesEvaluationServiceImpl) service;

        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(67L);
        student2Resource.setSceneId(40003);
        Commit commit = new Commit();
        commit.setStudentId(67L);
        commit.setSceneId(40003);

        CommitVO commitVO = impl.evaluate(student2Resource, commit);
        Double score = commitVO.getScore();
        System.out.println("compare得分: " + score);

    }
}