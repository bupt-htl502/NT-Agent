package com.coldwindx.server.utils;

import com.coldwindx.server.entity.enums.StaticFeature;
import com.coldwindx.server.entity.form.ResultAndFeatures;
import com.coldwindx.server.entity.form.experimentResult;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;
import org.jetbrains.annotations.NotNull;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.*;

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

    public static ResultAndFeatures loadFromCSV(String csvPath,String datatype) throws IOException, CsvValidationException {
        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
            List<String[]> allData = reader.readAll();
            if (allData.isEmpty()) {
                throw new IllegalArgumentException("CSV is empty.");
            }

            String[] header = allData.get(0);
            int featureCount = header.length - 1;

            if (datatype.equals("double")) {
                Map<String, double[]> doubleMap = new HashMap<>();

                for (String[] row : allData.subList(1, allData.size())) {
                    String key = row[0];
                    double[] values = new double[featureCount];
                    for (int i = 0; i < featureCount; i++) {
                        values[i] = Double.parseDouble(row[i + 1]);
                    }
                    doubleMap.put(key, values);
                }

                return new ResultAndFeatures(header, doubleMap);

            } else if (datatype.equals("String")) {
                Map<String, String[]> stringMap = new HashMap<>();

                for (String[] row : allData.subList(1, allData.size())) {
                    String key = row[0];
                    String[] values = new String[featureCount];
                    System.arraycopy(row, 1, values, 0, featureCount);
                    stringMap.put(key, values);
                }

                return new ResultAndFeatures(header, stringMap,true);
            } else {
                throw new UnsupportedOperationException("Unsupported type: " + datatype);
            }

        } catch (CsvException e) {
            throw new RuntimeException(e);
        }
    }

    public static experimentResult compareValuesNew(ResultAndFeatures result,ResultAndFeatures answer,String datatype){
        experimentResult experimentresult = new experimentResult();
        int incorrectCount = 0;  // 误差范围外的特征数目
        int totalCount = 0;  // 总共的特征数目
        double errorRange = 1e-3;  // 误差范围， 误差大于该值则认为数值不准确
        int commentThreshold = 3;  // 提示阈值，当某类特征错误数量超过该阈值，需要在comment中提及该特征
        Map<String, Integer> result_feature_pos = result.GetFeaturesPosMap();
        Map<String, Integer> answer_feature_pos = answer.GetFeaturesPosMap();
        int[] errorCount = new int[answer_feature_pos.size()];
        String[] feature_list = answer.GetFeaturesList();

        if(datatype.equals("double")) {
            Map<String, double[]> answer_double_map = answer.getDoubleMap();
            Map<String, double[]> result_double_map = result.getDoubleMap();

            for(String filename: answer_double_map.keySet()) {
                double[] featureResult = result_double_map.get(filename);
                double[] featureAnswer = answer_double_map.get(filename);

                for(int i=1; i<feature_list.length; ++i) {
                    int answer_pos = answer_feature_pos.get(feature_list[i])-1;
                    int result_pos = result_feature_pos.get(feature_list[i])-1;
                    double errorValue = Math.abs((featureAnswer[answer_pos]-featureResult[result_pos]) * (featureAnswer[answer_pos]+featureResult[result_pos]));
                    if(errorValue > errorRange) {
                        incorrectCount++;
                        errorCount[answer_pos]++;
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
            System.out.println("最终得分为：");
            System.out.println(experimentresult.score);
        }

        else if(datatype.equals("String")) {
            //每条数据的正确性的分数为100/data_length/2
            //每条数据的特征正确的分数为100/data_length/2/feature_length
            double score = 0;
            int feature_length = feature_list.length-2;
            Map<String, String[]> answer_string_map = answer.getStringMap();
            Map<String, String[]> result_string_map = result.getStringMap();

            int data_length = answer_string_map.size();
            double feature_score;
            double res_score;
            if(feature_length==0){
                feature_score = 0.0;
                res_score =(100.0/data_length);
            }else{
                feature_score = ((100.0 / data_length) / 2.0) / feature_length;
                res_score =(100.0/data_length)/2.0;
            }
//            System.out.println(feature_score);
//            System.out.println(res_score);
            for(String filename: answer_string_map.keySet()) {
                String[] featureResult = result_string_map.get(filename);
                String[] featureAnswer = answer_string_map.get(filename);

                for(int i=1; i<feature_list.length-1; ++i) {
                    int answer_pos = answer_feature_pos.get(feature_list[i])-1;
                    int result_pos = result_feature_pos.get(feature_list[i])-1;
                    String result_string = featureResult[result_pos];
                    String answer_string = featureAnswer[answer_pos];
                    if(answer_string.equals(result_string)) {
                        score+=feature_score;
                    }else {
                        errorCount[answer_pos]++;
                    }
                }
                int finnal_answer_pos = answer_feature_pos.get(feature_list[feature_list.length-1])-1;
                int finnal_result_pos = result_feature_pos.get(feature_list[feature_list.length-1])-1;
                String result_string = featureResult[finnal_result_pos];
                String answer_string = featureAnswer[finnal_answer_pos];
                if(answer_string.equals(result_string)) {
                    score+=res_score;
                }else {
                    errorCount[finnal_answer_pos]++;
                }
            }

            experimentresult.score = score;
//            System.out.println("最终得分为：");
//            System.out.println(score);
        }
        return experimentresult;
    }

}
