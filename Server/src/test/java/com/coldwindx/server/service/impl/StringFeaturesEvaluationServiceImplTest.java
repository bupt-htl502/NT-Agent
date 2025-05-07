package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
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
    void loadFromCSV() throws CsvException, IOException {
        System.out.println("loadFromCSV");
        StringFeaturesEvaluationServiceImpl impl = (StringFeaturesEvaluationServiceImpl) service;
        impl.loadFromCSV("/home/zhulin/workspace/NT-Agent/Sources/answerTest.csv");
        System.out.println("loadFromCSV");
    }

    @Test
    void testBeforeCompare() throws CsvException, IOException {
        StringFeaturesEvaluationServiceImpl impl = (StringFeaturesEvaluationServiceImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("/home/zhulin/workspace/NT-Agent/Sources/answerTest.csv");
        Commit commit = new Commit();
        commit.setPath("/home/zhulin/workspace/NT-Agent/Sources/resultTest.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);

        System.out.println("beforeCompare results size: " + standards.size());
        System.out.println("beforeCompare results size: " + results.size());
    }

    @Test
    void testCompare() throws CsvException, IOException {
        StringFeaturesEvaluationServiceImpl impl = (StringFeaturesEvaluationServiceImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("/home/zhulin/workspace/NT-Agent/Sources/answerTest.csv");
        Commit commit = new Commit();
        commit.setPath("/home/zhulin/workspace/NT-Agent/Sources/resultTest.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);

        double score = impl.compare(results, standards);
        System.out.println("compare得分: " + score);
        Assertions.assertTrue(score >= 0, "得分应该是非负数");

    }
}