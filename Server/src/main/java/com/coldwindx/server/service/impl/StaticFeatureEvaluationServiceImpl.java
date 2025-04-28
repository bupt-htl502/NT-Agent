package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service()
public class StaticFeatureEvaluationServiceImpl extends EffectEvaluationService {

    @Override
    public double compare(Map<String, Object> results, Map<String, Object> standards, Student2Resource student2Resource) {
        return 0;
    }

    @Override
    protected Map<String, Object> beforeCompare(Student2Resource student2Resource) {
        return Map.of();
    }
}
