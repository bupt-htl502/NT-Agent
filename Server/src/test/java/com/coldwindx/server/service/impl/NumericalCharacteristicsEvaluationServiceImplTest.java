package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.mapper.Student2ResourceMapper;
import com.coldwindx.server.service.CommitService;
import com.coldwindx.server.service.EffectEvaluationService;
import com.opencsv.exceptions.CsvException;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class NumericalCharacteristicsEvaluationServiceImplTest {
    @Autowired
    private CommitService commitService;

    @Resource(name = "numericalCharacteristicsEvaluationServiceImpl")
    private EffectEvaluationService service;
    @Test
    void loadFromCSV() throws Exception {

        System.out.println("loadFromCSV");

        NumericalCharacteristicsEvaluationServiceImpl impl = (NumericalCharacteristicsEvaluationServiceImpl) service;

        impl.loadDoubleFromCSV("temporary/resultTest.csv");

        System.out.println("loadFromCSV");

    }

    @Test
    void testBeforeCompare() throws Exception {
        NumericalCharacteristicsEvaluationServiceImpl impl = (NumericalCharacteristicsEvaluationServiceImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setCriterion("temporary/answerTest.csv");
        Commit commit = new Commit();
        commit.setPath("temporary/resultTest.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);

        System.out.println("beforeCompare standards size: " + standards.size());
        System.out.println("beforeCompare results size: " + results.size());
    }

    @Test
    void testCompare() throws Exception {
        NumericalCharacteristicsEvaluationServiceImpl impl = (NumericalCharacteristicsEvaluationServiceImpl) service;
        Student2Resource resource = new Student2Resource();
        resource.setCriterion("temporary/answerTest1.csv");
        Commit commit = new Commit();
        commit.setPath("temporary/resultTest-withoutline-82.csv");

        Map<String, Object> standards = impl.getStandard(resource);
        Map<String, Object> results = impl.getResult(commit);

        CommitVO commitVO = impl.compare(results,standards);
        Double score = commitVO.getScore();
        System.out.println(commitVO.getRemark());
        System.out.println("compare得分: " + score);
    }

//    @Test
//    void serverTest() throws Exception {
//        Commit commit = new Commit();
//        commit.setStudentId(67L);
//        commit.setSceneId(40003);
//        commit.setScore(0.0);
//        commit.setPath("temporary/resultTest.csv");
//        commit.setCreateTime(1234L);
//        CommitVO commitVO = commitService.insert(commit);
//        System.out.println(commitVO.getScore());
//        System.out.println(commitVO.getRemark());
//    }
}