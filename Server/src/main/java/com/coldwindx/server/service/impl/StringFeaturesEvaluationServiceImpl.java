package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.CommitVO;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import com.coldwindx.server.utils.EvaluateUtils;
import com.coldwindx.server.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service
public class StringFeaturesEvaluationServiceImpl extends EffectEvaluationService {

    @Autowired
    private MinioUtils minioUtils;

    @Override
    public CommitVO compare(Map<String, Object> results, Map<String, Object> standards) {
        int dataLength = standards.size();
        Map.Entry<String, Object> firstEntry = standards.entrySet().iterator().next();
        long featureLength = Arrays.stream((Object[]) firstEntry.getValue()).count();
        double featureScore = featureLength == 0 ? 0.0 : (100.0 / dataLength / featureLength);
        // 1. 合并两个map的key
        Map<String, Integer> errorMap = new HashMap<>();
        Stream<String> keys = Stream.of(results, standards).flatMap(map -> map.keySet().stream()).distinct();
        Double score =  keys.mapToDouble(key->{
            String[] featureResult = Arrays.stream((Object[]) results.get(key)).map(it->(String)it).toArray(String[]::new);
            String[] featureAnswer = Arrays.stream((Object[]) standards.get(key)).map(it->(String)it).toArray(String[]::new);
            errorMap.put(key,0);
            return IntStream.range(0, featureAnswer.length).mapToDouble(i->{
                if(!featureResult[i].equals(featureAnswer[i])) {
                    errorMap.put(key,errorMap.get(key)+1);
                    return 0.0;
                }
                return featureScore;
            }).sum();
        }).sum();
        CommitVO commitVO = new CommitVO();
        commitVO.setScore(score);
        EvaluateUtils evaluateUtils = new EvaluateUtils();
        commitVO.setRemark(evaluateUtils.comment(errorMap));
        System.out.println("123");
        return commitVO;
    }

    @Override
    protected Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception {
        return loadFromCSV(student2Resource.getCriterion());
    }

    @Override
    protected Map<String, Object> getResult(Commit commit) throws Exception {
        return loadFromCSV(commit.getPath());
    }

    protected Map<String, Object> loadFromCSV(String csv) throws Exception {
        String[] tempName = csv.split("/",2);
        List<String[]> allData = minioUtils.getCSVData(tempName);

        if (allData.isEmpty()) {
            throw new IllegalArgumentException("CSV is empty.");
        }

        return allData.stream().skip(1).map(row -> {
            Object[] results = Arrays.stream(row).skip(1).toArray();
            return new AbstractMap.SimpleEntry<>(row[0], results);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

}
