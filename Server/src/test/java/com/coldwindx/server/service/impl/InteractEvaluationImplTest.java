package com.coldwindx.server.service.impl;

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
class InteractEvaluationImplTest {
    @Resource(name = "interactEvaluationImpl")
    private EffectEvaluationService service;
    @Test
    void loadFromCSV() throws CsvException, IOException {
        System.out.println("loadFromCSV");
        InteractEvaluationImpl impl = (InteractEvaluationImpl) service;
        impl.loadFromCSV("C:\\Users\\81595\\Desktop\\testdata\\string.csv");
        System.out.println("loadFromCSV");
    }

    @Test
    void testBeforeCompare() throws CsvException, IOException {
        InteractEvaluationImpl impl = (InteractEvaluationImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setPath("C:\\Users\\81595\\Desktop\\testdata\\string.csv");
        resource.setCriterion("C:\\Users\\81595\\Desktop\\testdata\\string1.csv");

        Map<String, Object> args = impl.beforeCompare(resource);

        Assertions.assertNotNull(args, "beforeCompare()返回值不能为null");
        Assertions.assertTrue(args.containsKey("results"), "beforeCompare()返回Map中应包含results");
        Assertions.assertTrue(args.containsKey("standards"), "beforeCompare()返回Map中应包含standards");

        System.out.println("beforeCompare results size: " + ((Map<?, ?>) args.get("results")).size());
        System.out.println("beforeCompare results size: " + ((Map<?, ?>) args.get("standards")).size());
    }

    @Test
    void testCompare() throws CsvException, IOException {
        InteractEvaluationImpl impl = (InteractEvaluationImpl) service;

        Student2Resource resource = new Student2Resource();
        resource.setPath("/home/zhulin/workspace/NT-Agent/Sources/resultTest.csv");
        resource.setCriterion("/home/zhulin/workspace/NT-Agent/Sources/answerTest.csv");

        Map<String, Object> args = impl.beforeCompare(resource);
        Map<String, Object> results = (Map<String, Object>) args.get("results");
        Map<String, Object> standards = (Map<String, Object>) args.get("standards");

        double score = impl.compare(results, standards);
        System.out.println("compare得分: " + score);
        Assertions.assertTrue(score >= 0, "得分应该是非负数");

    }
}