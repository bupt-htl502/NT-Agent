package com.coldwindx.server.utils;

import com.coldwindx.server.entity.enums.StaticFeature;
import com.coldwindx.server.entity.form.experimentResult;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ScoreUtils {
    /**
     * 从csv文件中读入数据
     */
    public static Map<String, double[]> loadValueFromCSV(String csvPath) throws IOException, CsvValidationException {
        Map<String, double[]> answersMap = new HashMap<>();
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> allData = reader.readAll();
            if (allData.isEmpty()) {
                throw new IllegalArgumentException("Answer CSV file is empty.");
            }
            String[] header = allData.get(0); // Header row (can be ignored for processing if not needed)
            int resultColumnCount = header.length - 1; // Assuming the first column is the filename

            for (String[] row : allData.subList(1, allData.size())) { // Skip header
                String fileName = row[0];
                double[] results = new double[resultColumnCount];
                for (int i = 0; i < resultColumnCount; i++) {
                    if (row[i+1] != "") {
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

    public static experimentResult compareValues(
            Map<String, double[]> result, Map<String, double[]> answer, String valueCategory) {
        experimentResult experimentresult = new experimentResult();
        int incorrectCount = 0;  // 误差范围外的特征数目
        int totalCount = 0;  // 总共的特征数目
        double errorRange = 1e-3;  // 误差范围， 误差大于该值则认为数值不准确
        int commentThreshold = 3;  // 提示阈值，当某类特征错误数量超过该阈值，需要在comment中提及该特征
        List<String> commentFeature = new ArrayList<>();
        int[] errorCount = new int[0];
        if (valueCategory == "StaticFeature") {
            errorCount = new int[StaticFeature.values().length];
        }
        for(String filename: answer.keySet()) {
            double[] featureResult = result.get(filename);
            double[] featureAnswer = answer.get(filename);
//            if
            for(int i=0; i<featureAnswer.length; ++i) {
                double errorValue = Math.abs((featureAnswer[i]-featureResult[i]) * (featureAnswer[i]+featureResult[i]));
                if(errorValue > errorRange) {
                    incorrectCount++;
                    errorCount[i]++;
                }
                totalCount++;
            }
        }
        for(int i=0; i<errorCount.length; ++i){
            if(errorCount[i] > commentThreshold) {
                incorrectCount += 5;
            }
        }
        experimentresult.score = 100 - incorrectCount;
        return experimentresult;
    }

    public static void main(String[] args) throws CsvValidationException, IOException {
        String resultCSVPath = "E:/test/resultTest.csv";
        String answerCSVPath = "E:/test/answerTest.csv";
        Map<String, double[]> resultMap = loadValueFromCSV(resultCSVPath);
        Map<String, double[]> answerMap = loadValueFromCSV(answerCSVPath);
        experimentResult test = compareValues(resultMap, answerMap, "StaticFeature");
        System.out.println(test.score);

    }
}
