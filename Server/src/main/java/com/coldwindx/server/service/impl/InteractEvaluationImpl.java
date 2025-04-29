package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.springframework.stereotype.Service;

import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service()
public class InteractEvaluationImpl extends EffectEvaluationService {

    @Override
    public double compare(Map<String, Object> results, Map<String, Object> standards) {
        int dataLength = standards.size();
        double featureScore;
        double resScore;
        Map.Entry<String, Object> firstEntry = standards.entrySet().iterator().next();
        // 获取该元素的值，假设是一个数组（可以根据你的实际情况调整）
        Object firstValue = firstEntry.getValue();
        double score = 0;
        String[] array = (String[]) firstValue;
        int featureLength = array.length-1;
        if(featureLength==0){
            return -1;
        }
        if(featureLength==0){
            featureScore = 0.0;
            resScore =(100.0/dataLength);
        }else{
            featureScore = ((100.0 / dataLength) / 2.0) / featureLength;
            resScore =(100.0/dataLength)/2.0;
        }
        
        for(String filename: standards.keySet()) {
            String[] featureResult = (String[]) results.get(filename);
            String[] featureAnswer = (String[]) standards.get(filename);

            for(int i=0; i<featureLength+1; ++i) {
                String resultString = featureResult[i];
                String answerString = featureAnswer[i];
                if(answerString.equals(resultString)&&i==featureLength-1) {
                    score+=resScore;
                }else if(answerString.equals(resultString)) {
                    score+=featureScore;
                }
            }

        }
        return score;
    }

    @Override
    protected Map<String, Object> beforeCompare(Student2Resource student2Resource) throws CsvValidationException, IOException {
        Map<String, Object> results = loadFromCSV(student2Resource.getPath());
        Map<String, Object> standards = loadFromCSV(student2Resource.getCriterion());
        Map<String, Object> args = new HashMap<>();
        args.put("results", results);
        args.put("standards", standards);
        return args;
    }

    public static Map<String, Object> loadFromCSV(String csvPath) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> allData = reader.readAll();
            if (allData.isEmpty()) {
                throw new IllegalArgumentException("CSV is empty.");
            }

            String[] header = allData.getFirst();
            int featureCount = header.length - 1;
            Map<String, Object> newMap = new HashMap<>();

            for (String[] row : allData.subList(1, allData.size())) {
                String key = row[0];
                String[] values = new String[featureCount];
                System.arraycopy(row, 1, values, 0, featureCount);
                newMap.put(key, values);
            }
            return newMap;
        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

}
