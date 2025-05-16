package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import com.coldwindx.server.utils.EvaluateUtils;
import com.coldwindx.server.utils.MinioUtils;
import com.fasterxml.jackson.databind.util.internal.PrivateMaxEntriesMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class NumericalCharacteristicsEvaluationServiceImpl extends EffectEvaluationService {

    private final double DIS = 1e-3;      // 误差范围， 误差大于该值则认为数值不准确
    private final int THRESHOLD = 3;      // 提示阈值，当某类特征错误数量超过该阈值，需要在comment中提及该特征
    @Autowired
    private MinioUtils minioUtils;

    @Override
    public CommitVO compare(Map<String, Object> results, Map<String, Object> standards) {
        // 1. 合并两个map的key
        Map<String, Integer> errorMap = new HashMap<>();
        // 2. 根据key计算误差特征数量
        List<double[]> scores = standards.keySet().stream().map(key -> {
            double[] featureAnswer = Arrays.stream((Object[]) standards.get(key))
                    .mapToDouble(it -> (double) it)
                    .toArray();

            double[] featureResult;
            if (results.containsKey(key)) {
                featureResult = Arrays.stream((Object[]) results.get(key))
                        .mapToDouble(it -> (double) it)
                        .toArray();
            } else {
                featureResult = new double[featureAnswer.length];
                Arrays.fill(featureResult, -100.0);
            }

            double[] scoreArray = IntStream.range(0, featureAnswer.length)
                    .mapToDouble(i -> Math.abs((featureAnswer[i] - featureResult[i]) * (featureAnswer[i] + featureResult[i])))
                    .toArray();

            // 计算误差数量（每个特征分值 <= DIS 视为误差）
            int errorCount = (int) Arrays.stream(scoreArray).filter(v -> v > DIS).count();
            errorMap.put(key, errorCount);

            return scoreArray;
        }).toList();

        // 3. 计算整体分数
        long sum = scores.stream().mapToLong(l-> Arrays.stream(l).filter(v->DIS < v).count()).sum();
        long count = 0;
        for (Integer value :errorMap.values()) {
            if(value>THRESHOLD){
                count++;
            }
        }
        CommitVO commitVO = new CommitVO();
        commitVO.setScore((double) (100 - sum - 5 * count));
        EvaluateUtils evaluateUtils = new EvaluateUtils();
        commitVO.setRemark(evaluateUtils.comment(errorMap));
        return commitVO;
    }

    @Override
    protected Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception {
        return loadDoubleFromCSV(student2Resource.getCriterion());
    }

    @Override
    protected Map<String, Object> getResult(Commit commit) throws Exception {
        return loadDoubleFromCSV(commit.getPath());
    }

    protected Map<String, Object> loadDoubleFromCSV(String csv) throws Exception {
        String[] tempName = csv.split("/",2);
        List<String[]> allData = minioUtils.getCSVData(tempName);

        return allData.stream().skip(1).map(row -> {
            Object[] results = Arrays.stream(row).skip(1).map(it -> {
                try {
                    return Double.parseDouble(it);
                } catch (Exception e) {
                    return 0.0;
                }
            }).toArray();
            return new AbstractMap.SimpleEntry<>(row[0], results);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}

