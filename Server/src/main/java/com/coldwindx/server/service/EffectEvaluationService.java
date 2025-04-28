package com.coldwindx.server.service;

import com.coldwindx.server.entity.form.Student2Resource;

import java.io.IOException;
import java.util.Map;

abstract public class EffectEvaluationService {
    /**
     * 效果评估服务
     * @param results       用户结果
     * @param standards     标准结果
     * @param student2Resource        额外参数
     * @return  效果评估结果分数
     */
    public abstract double compare(Map<String, Object> results, Map<String, Object> standards, Student2Resource student2Resource);

    protected abstract Map<String, Object> beforeCompare(Student2Resource student2Resource);

    protected Map<String, Object> afterCompare(double score, Student2Resource student2Resource){
        return null;
    }

    public double evaluate(Student2Resource student2Resource) throws IOException {
        Map<String, Object> args = beforeCompare(student2Resource);
        Map<String, Object> results = (Map<String, Object>) args.get("results");
        Map<String, Object> standards = (Map<String, Object>) args.get("standards");
        double score = this.compare(results, standards, student2Resource);
        afterCompare(score, student2Resource);
        return score;
    }
}
