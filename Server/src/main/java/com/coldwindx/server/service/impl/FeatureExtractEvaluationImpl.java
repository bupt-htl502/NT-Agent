package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

@Service()
public class FeatureExtractEvaluationImpl extends EffectEvaluationService {

    private final double DIS = 1e-5;      // 误差范围， 误差大于该值则认为数值不准确
    private final int THRESHOLD = 3;      // 提示阈值，当某类特征错误数量超过该阈值，需要在comment中提及该特征

    @Override
    public double compare(Map<String, Object> results, Map<String, Object> standards) {
        // 1. 合并两个map的key
        Stream<String> keys = Stream.of(results, standards).flatMap(map -> map.keySet().stream()).distinct();
        // 2. 根据key计算误差特征数量
        List<Long> counts = keys.map(key -> {
            Double[] featureResult = (Double[]) results.get(key);
            Double[] featureAnswer = (Double[]) standards.get(key);
            return IntStream.range(0, featureAnswer.length)
                    .mapToDouble(i -> Math.abs((featureAnswer[i] - featureResult[i]) * (featureAnswer[i] + featureResult[i])))
                    .filter(v->DIS < v)
                    .count();
        }).toList();

        // 3. 计算整体分数
        long sum = counts.stream().mapToLong(it->it).sum();
        long count = counts.stream().mapToLong(it->it).filter(v-> THRESHOLD < v).count();
        return 100 - sum - 5 * count;
    }

    @Override
    protected Map<String, Object> beforeCompare(Student2Resource student2Resource) throws IOException, CsvException {
        Map<String, Object> results = loadDoubleFromCSV(student2Resource.getPath());
        Map<String, Object> standards = loadDoubleFromCSV(student2Resource.getCriterion());
        Map<String, Object> args = new HashMap<>();
        args.put("results", results);
        args.put("standards", standards);
        return args;
    }

    protected Map<String, Object> loadDoubleFromCSV(String csv) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(csv));
        List<String[]> allData = reader.readAll();
        if (allData.isEmpty())
            throw new IllegalArgumentException("Answer CSV file is empty.");

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

