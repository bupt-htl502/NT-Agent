package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.enums.StaticFeature;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.entity.form.experimentResult;
import com.coldwindx.server.service.EffectEvaluationService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service()
public class FeatureExtractEvaluationImpl extends EffectEvaluationService {

    @Override
    public double compare(Map<String, Object> results, Map<String, Object> standards, Student2Resource student2Resource) {
        int incorrectCount = 0;  // 误差范围外的特征数目
        double errorRange = 1e-3;  // 误差范围， 误差大于该值则认为数值不准确
        int commentThreshold = 3;  // 提示阈值，当某类特征错误数量超过该阈值，需要在comment中提及该特征
        int[] errorCount;
        Map.Entry<String, Object> firstEntry = standards.entrySet().iterator().next();
        // 获取该元素的值，假设是一个数组（可以根据你的实际情况调整）
        Object firstValue = firstEntry.getValue();
        double score = 0;
        Double[] array = (Double[]) firstValue;
        int featureLength = array.length-1;
        errorCount = new int[featureLength+1];
        for(String filename: standards.keySet()) {
            Double[] featureResult = (Double[]) results.get(filename);
            Double[] featureAnswer = (Double[]) standards.get(filename);
            for(int i=0; i<featureAnswer.length; ++i) {
                double errorValue = Math.abs((featureAnswer[i]-featureResult[i]) * (featureAnswer[i]+featureResult[i]));
                if(errorValue > errorRange) {
                    incorrectCount++;
                    errorCount[i]++;
                }
            }
        }
        for (int j : errorCount) {
            if (j > commentThreshold) {
                incorrectCount += 5;
            }
        }
        score = 100 - incorrectCount;
        return score;
    }

    @Override
    protected Map<String, Object> beforeCompare(Student2Resource student2Resource) throws CsvValidationException, IOException {
        Map<String, Object> results = loadDoubleFromCSV(student2Resource.getPath());
        Map<String, Object> standards = loadDoubleFromCSV(student2Resource.getCriterion());
        Map<String, Object> args = new HashMap<>();
        args.put("results", results);
        args.put("standards", standards);
        return args;

    }
    public static Map<String, Object> loadDoubleFromCSV(String csvPath) throws IOException, CsvValidationException {
        Map<String, Object> answersMap = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> allData = reader.readAll();
            if (allData.isEmpty()) {
                throw new IllegalArgumentException("Answer CSV file is empty.");
            }
            String[] header = allData.getFirst(); // Header row (can be ignored for processing if not needed)
            int resultColumnCount = header.length - 1; // Assuming the first column is the filename
            for (String[] row : allData.subList(1, allData.size())) { // Skip header
                String fileName = row[0];
                Double[] results = new Double[resultColumnCount];
                for (int i = 0; i < resultColumnCount; i++) {
                    if (!row[i + 1].isEmpty()) {
                        results[i] = Double.parseDouble(row[i + 1]);
                    } else {
                        results[i] = 0.0;
                    }
                }
                answersMap.put(fileName, results);
            }
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
        return answersMap;
    }
}

