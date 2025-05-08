package com.coldwindx.server.service;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.mapper.Student2ResourceMapper;
import jakarta.annotation.Resource;

import java.util.Map;

public abstract class EffectEvaluationService {
    @Resource
    private Student2ResourceMapper student2ResourceMapper;
    @Resource
    private CommitMapper commitMapper;
    /**
     * 效果评估服务
     * @param results       用户结果
     * @param standards     标准结果
     * @return  效果评估结果分数
     */
    public abstract double compare(Map<String, Object> results, Map<String, Object> standards);

    protected abstract Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception;

    protected abstract Map<String, Object> getResult(Commit commit) throws Exception;

    protected void afterCompare(double score, Commit commit){
        commit.setScore(score);
        commitMapper.update(commit);
    }

    public double evaluate(Student2Resource student2Resource, Commit commit) throws Exception {
        Map<String, Object> results = getResult(commit);
        Map<String, Object> standards = getStandard(student2Resource);
        double score = compare(results, standards);
        afterCompare(score, commit);
        return score;
    }
}
