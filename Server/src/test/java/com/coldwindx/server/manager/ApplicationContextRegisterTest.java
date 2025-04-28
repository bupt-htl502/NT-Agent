package com.coldwindx.server.manager;

import com.coldwindx.server.service.EffectEvaluationService;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ApplicationContextRegisterTest {
    @Test
    void getBean() {
        EffectEvaluationService service = (EffectEvaluationService) ApplicationContextRegister.getBean("staticFeatureEvaluationService");
        double score = service.compare(null, null, null);
        System.out.println(score);
    }
}