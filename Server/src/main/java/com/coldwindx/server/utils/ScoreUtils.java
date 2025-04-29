package com.coldwindx.server.utils;

import com.coldwindx.server.entity.enums.StaticFeature;
import com.coldwindx.server.entity.form.ResultAndFeatures;
import com.coldwindx.server.entity.form.experimentResult;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import com.opencsv.exceptions.CsvValidationException;

import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class ScoreUtils {
    /**
     * 从csv文件中读入数据
     */
//    public static Map<String, double[]> loadValueFromCSV(String csvPath) throws IOException, CsvValidationException {
//        Map<String, double[]> answersMap = new HashMap<>();
//        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
//            List<String[]> allData = reader.readAll();
//            if (allData.isEmpty()) {
//                throw new IllegalArgumentException("Answer CSV file is empty.");
//            }
//            String[] header = allData.get(0); // Header row (can be ignored for processing if not needed)
//            int resultColumnCount = header.length - 1; // Assuming the first column is the filename
//
//            for (String[] row : allData.subList(1, allData.size())) { // Skip header
//                String fileName = row[0];
//                double[] results = new double[resultColumnCount];
//                for (int i = 0; i < resultColumnCount; i++) {
//                    if (row[i+1] != "") {
//                        results[i] = Double.parseDouble(row[i + 1]);
//                    } else {
//                        results[i] = 0.0;
//                    }
//                }
//                answersMap.put(fileName, results);
//            }
//        } catch (CsvException e) {
//            throw new RuntimeException(e);
//        }
//        return answersMap;
//    }

//    public static experimentResult compareValues(
//            Map<String, double[]> result, Map<String, double[]> answer, String valueCategory) {
//        experimentResult experimentresult = new experimentResult();
//        int incorrectCount = 0;  // 误差范围外的特征数目
//        int totalCount = 0;  // 总共的特征数目
//        double errorRange = 1e-3;  // 误差范围， 误差大于该值则认为数值不准确
//        int commentThreshold = 3;  // 提示阈值，当某类特征错误数量超过该阈值，需要在comment中提及该特征
//        List<String> commentFeature = new ArrayList<>();
//        int[] errorCount = new int[0];
//
//        if (valueCategory == "StaticFeature") {
//            errorCount = new int[StaticFeature.values().length];
//        }
//
//        for(String filename: answer.keySet()) {
//            double[] featureResult = result.get(filename);
//            double[] featureAnswer = answer.get(filename);
//            for(int i=0; i<featureAnswer.length; ++i) {
//                double errorValue = Math.abs((featureAnswer[i]-featureResult[i]) * (featureAnswer[i]+featureResult[i]));
//                if(errorValue > errorRange) {
//                    incorrectCount++;
//                    errorCount[i]++;
//                }
//                totalCount++;
//            }
//        }
//        for(int i=0; i<errorCount.length; ++i){
//            if(errorCount[i] > commentThreshold) {
//                incorrectCount += 5;
//            }
//        }
//        experimentresult.score = 100 - incorrectCount;
//        return experimentresult;
//    }

//    public static ResultAndFeatures loadFromCSV(String csvPath,String datatype) throws IOException, CsvValidationException {
//        try (CSVReader reader = new CSVReader(new FileReader(csvPath))) {
//            List<String[]> allData = reader.readAll();
//            if (allData.isEmpty()) {
//                throw new IllegalArgumentException("CSV is empty.");
//            }
//
//            String[] header = allData.get(0);
//            int featureCount = header.length - 1;
//
//            if (datatype.equals("double")) {
//                Map<String, double[]> doubleMap = new HashMap<>();
//
//                for (String[] row : allData.subList(1, allData.size())) {
//                    String key = row[0];
//                    double[] values = new double[featureCount];
//                    for (int i = 0; i < featureCount; i++) {
//                        values[i] = Double.parseDouble(row[i + 1]);
//                    }
//                    doubleMap.put(key, values);
//                }
//
//                return new ResultAndFeatures(header, doubleMap);
//
//            } else if (datatype.equals("String")) {
//                Map<String, String[]> stringMap = new HashMap<>();
//
//                for (String[] row : allData.subList(1, allData.size())) {
//                    String key = row[0];
//                    String[] values = new String[featureCount];
//                    System.arraycopy(row, 1, values, 0, featureCount);
//                    stringMap.put(key, values);
//                }
//
//                return new ResultAndFeatures(header, stringMap,true);
//            } else {
//                throw new UnsupportedOperationException("Unsupported type: " + datatype);
//            }
//
//        } catch (CsvException e) {
//            throw new RuntimeException(e);
//        }
//    }

    public static experimentResult compareValuesNew(ResultAndFeatures result,ResultAndFeatures answer,String datatype){
        experimentResult experimentresult = new experimentResult();
        int incorrectCount = 0;  // 误差范围外的特征数目
        int totalCount = 0;  // 总共的特征数目
        double errorRange = 1e-3;  // 误差范围， 误差大于该值则认为数值不准确
        int commentThreshold = 3;  // 提示阈值，当某类特征错误数量超过该阈值，需要在comment中提及该特征
        Map<String, Integer> result_feature_pos = result.GetFeaturesPosMap();
        Map<String, Integer> answer_feature_pos = answer.GetFeaturesPosMap();
        int[] errorCount = new int[answer_feature_pos.size()];
        String[] featureList = answer.GetFeaturesList();

        if(datatype.equals("double")) {
            Map<String, double[]> answer_double_map = answer.getDoubleMap();
            Map<String, double[]> result_double_map = result.getDoubleMap();

            for(String filename: answer_double_map.keySet()) {
                double[] featureResult = result_double_map.get(filename);
                double[] featureAnswer = answer_double_map.get(filename);

                for(int i=1; i<featureList.length; ++i) {
                    int answerPos = answer_feature_pos.get(featureList[i])-1;
                    int resultPos = result_feature_pos.get(featureList[i])-1;
                    double errorValue = Math.abs((featureAnswer[answerPos]-featureResult[resultPos]) * (featureAnswer[answerPos]+featureResult[resultPos]));
                    if(errorValue > errorRange) {
                        incorrectCount++;
                        errorCount[answerPos]++;
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
//            System.out.println("最终得分为：");
//            System.out.println(experimentresult.score);
        }

        else if(datatype.equals("String")) {
            //每条数据的正确性的分数为100/data_length/2
            //每条数据的特征正确的分数为100/data_length/2/feature_length
            double score = 0;
            int feature_length = featureList.length-2;
            Map<String, String[]> answerStringMap = answer.getStringMap();
            Map<String, String[]> resultStringMap = result.getStringMap();

            int data_length = answerStringMap.size();
            double featureScore;
            double resScore;
            if(feature_length==0){
                featureScore = 0.0;
                resScore =(100.0/data_length);
            }else{
                featureScore = ((100.0 / data_length) / 2.0) / feature_length;
                resScore =(100.0/data_length)/2.0;
            }
//            System.out.println(featureScore);
//            System.out.println(resScore);
            for(String filename: answerStringMap.keySet()) {
                String[] featureResult = resultStringMap.get(filename);
                String[] featureAnswer = answerStringMap.get(filename);

                for(int i=1; i<featureList.length-1; ++i) {
                    int answerPos = answer_feature_pos.get(featureList[i])-1;
                    int resultPos = result_feature_pos.get(featureList[i])-1;
                    String resultString = featureResult[resultPos];
                    String answerString = featureAnswer[answerPos];
                    if(answerString.equals(resultString)) {
                        score+=featureScore;
                    }else {
                        errorCount[answerPos]++;
                    }
                }
                int finnal_answerPos = answer_feature_pos.get(featureList[featureList.length-1])-1;
                int finnal_resultPos = result_feature_pos.get(featureList[featureList.length-1])-1;
                String resultString = featureResult[finnal_resultPos];
                String answerString = featureAnswer[finnal_answerPos];
                if(answerString.equals(resultString)) {
                    score+=resScore;
                }else {
                    errorCount[finnal_answerPos]++;
                }
            }

            experimentresult.score = score;
//            System.out.println("最终得分为：");
//            System.out.println(score);
        }
        return experimentresult;
    }

    //交互特征部分评估


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