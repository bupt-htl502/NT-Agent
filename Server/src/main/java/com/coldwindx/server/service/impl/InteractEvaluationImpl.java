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
public class InteractEvaluationImpl extends EffectEvaluationService {

    @Override
    public double compare(Map<String, Object> results, Map<String, Object> standards) {
        int dataLength = standards.size();
        Map.Entry<String, Object> firstEntry = standards.entrySet().iterator().next();
        long featureLength = Arrays.stream((Object[]) firstEntry.getValue()).skip(1).count();

        double featureScore = featureLength == 0 ? 0.0 : (50.0 / dataLength / featureLength);
        double resScore = featureLength == 0 ? (100.0 / dataLength) : (50.0 / dataLength);

        // 1. 合并两个map的key
        Stream<String> keys = Stream.of(results, standards).flatMap(map -> map.keySet().stream()).distinct();
        return keys.mapToDouble(key->{
            String[] featureResult = Arrays.stream((Object[]) results.get(key)).map(it->(String)it).toArray(String[]::new);
            String[] featureAnswer = Arrays.stream((Object[]) standards.get(key)).map(it->(String)it).toArray(String[]::new);
            return IntStream.range(0, featureAnswer.length).mapToDouble(i->{
                if(!featureResult[i].equals(featureAnswer[i]))
                    return 0.0;
                return i == featureAnswer.length - 1 ? resScore : featureScore;
            }).sum();
        }).sum();

    }

    @Override
    protected Map<String, Object> beforeCompare(Student2Resource student2Resource) throws IOException, CsvException {
        Map<String, Object> results = loadFromCSV(student2Resource.getPath());
        Map<String, Object> standards = loadFromCSV(student2Resource.getCriterion());
        Map<String, Object> args = new HashMap<>();
        args.put("results", results);
        args.put("standards", standards);
        return args;
    }

    protected Map<String, Object> loadFromCSV(String csv) throws IOException, CsvException {
        CSVReader reader = new CSVReader(new FileReader(csv));
        List<String[]> allData = reader.readAll();
        if (allData.isEmpty()) {
            throw new IllegalArgumentException("CSV is empty.");
        }

        return allData.stream().skip(1).map(row -> {
            Object[] results = Arrays.stream(row).skip(1).toArray();
            return new AbstractMap.SimpleEntry<>(row[0], results);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

    }

}
