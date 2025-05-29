package com.coldwindx.server.utils;

import com.coldwindx.server.manager.MinioMananger;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class MinioUtils {

    @Autowired
    private MinioMananger minioMananger;

    public List<String[]> getCSVData(String[] tempName) throws Exception {
        List<String[]> allData;
        String bucketName = tempName[0];
        String path = tempName[1];
        InputStream inputStream = minioMananger.getObject(bucketName,path);
        InputStreamReader streamReader = new InputStreamReader(inputStream, StandardCharsets.UTF_8); // 指定编码

        CSVReader reader = new CSVReader(streamReader);
        allData = reader.readAll();
        if (allData.isEmpty())
            throw new IllegalArgumentException("Answer CSV file is empty.");
        return allData;
    }
    public Map<String, Object> loadStringFromCSV(String csv) throws Exception {
        String[] tempName = csv.split("/", 2);
        List<String[]> allData = getCSVData(tempName);

        if (allData.isEmpty()) {
            throw new IllegalArgumentException("CSV is empty.");
        }

        return allData.stream().skip(1).map(row -> {
            Object[] results = Arrays.stream(row).skip(1).toArray();
            return new AbstractMap.SimpleEntry<>(row[0], results);
        }).collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
