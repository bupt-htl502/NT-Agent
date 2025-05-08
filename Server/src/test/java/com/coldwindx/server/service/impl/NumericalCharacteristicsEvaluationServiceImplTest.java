package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.mapper.Student2ResourceMapper;
import com.coldwindx.server.service.EffectEvaluationService;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NumericalCharacteristicsEvaluationServiceImplTest {
    @Resource(name = "numericalCharacteristicsEvaluationServiceImpl")
    private EffectEvaluationService service;
    @Test
    void loadFromCSV() throws CsvException, IOException {
        System.out.println("loadFromCSV");
        NumericalCharacteristicsEvaluationServiceImpl impl = (NumericalCharacteristicsEvaluationServiceImpl) service;
        impl.loadDoubleFromCSV("D:\\answerTest.csv");
        System.out.println("loadFromCSV");
    }

    @Test
    void testBeforeCompare() throws CsvException, IOException {
        NumericalCharacteristicsEvaluationServiceImpl impl = (NumericalCharacteristicsEvaluationServiceImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("D:\\answerTest.csv");
        Commit commit = new Commit();
        commit.setPath("D:\\resultTest.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);

        System.out.println("beforeCompare standards size: " + standards.size());
        System.out.println("beforeCompare results size: " + results.size());
    }

    @Test
    void testCompare() throws Exception {
        NumericalCharacteristicsEvaluationServiceImpl impl = (NumericalCharacteristicsEvaluationServiceImpl) service;

        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(10001L);
        student2Resource.setSceneId(1);
        Commit commit = new Commit();
        commit.setStudentId(10001L);
        commit.setSceneId(1);
        double score = impl.evaluate(student2Resource, commit);
        System.out.println("compare得分: " + score);
    }
}