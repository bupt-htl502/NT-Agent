package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.EffectEvaluationService;
import com.coldwindx.server.utils.EvaluateUtils;
import com.coldwindx.server.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service("multiCSVStringFeaturesEvaluationServiceImpl")
public class MultiCSVStringFeaturesEvaluationServiceImpl extends EffectEvaluationService {

//    读取全部答案目录下的csv文件
//    逐个比较同名csv中的全部内容，并记录，评分

    @Autowired
    private MinioUtils minioUtils;

    @Autowired
    private MinioMananger minioMananger;

    @Autowired
    private StringFeaturesEvaluationServiceImpl stringFeaturesEvaluationServiceImpl;
//  未完成 评估逻辑
    @Override
    public CommitVO compare(Map<String, Object> results, Map<String, Object> standards) {
        CommitVO commitVO = stringFeaturesEvaluationServiceImpl.compare(results, standards);
//        System.out.println("123123");
        return commitVO;
    }

    @Override
    protected Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception {
        return loadFromCSV(student2Resource.getCriterion());
    }

//  获取zip中的
    @Override
    protected Map<String, Object> getResult(Commit commit) throws Exception {
         String path = commit.getPath();
         String[] tempName = path.split("/",2);
        return readCsvZip(minioMananger.getObject(tempName[0],tempName[1]));
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


    //    读取解压缩目录下的全部csv文件
    public Map<String, Object> readCsvZip(InputStream zipInputStream) throws IOException {
        Map<String, Object> md5Map = new HashMap<>();
        EvaluateUtils evaluateUtils = new EvaluateUtils();
        try (ZipInputStream zis = new ZipInputStream(zipInputStream, StandardCharsets.UTF_8)) {
            ZipEntry entry;
            while ((entry = zis.getNextEntry()) != null) {
                if (!entry.isDirectory() && entry.getName().endsWith(".csv")) {

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    byte[] buffer = new byte[4096];
                    int len;
                    while ((len = zis.read(buffer)) != -1) {
                        baos.write(buffer, 0, len);
                    }
                    byte[] fileBytes = baos.toByteArray();

                    // 计算 MD5
                    String md5 = evaluateUtils.calculateMD5(fileBytes);
                    String [] temp = new String[1];
                    temp[0] = md5;
                    String [] name = entry.getName().split("/");
                    // 保存结果
                    md5Map.put(name[name.length-1], temp);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return md5Map;
    }
}
