package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
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

    @Resource
    private CommitMapper commitMapper;
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
    void testCompare() throws CsvException, IOException {
        NumericalCharacteristicsEvaluationServiceImpl impl = (NumericalCharacteristicsEvaluationServiceImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("D:\\answerTest.csv");
        Commit commit = new Commit();
        commit.setPath("D:\\resultTest.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);
        double score = impl.compare(results, standards);
        System.out.println("compare得分: " + score);
        Assertions.assertTrue(score >= 0, "得分应该是非负数");
    }

    @Test
    void testUpdate() throws CsvException, IOException {
        Commit commit = new Commit();
        commit.setStudentId(1L);
        commit.setSceneId(1);
        commit.setScore(50.00);
        commitMapper.update(commit);
    }
}