package com.coldwindx.server.utils;

import com.coldwindx.server.manager.MinioMananger;
import com.opencsv.CSVReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

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
}
