package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.EffectEvaluationService;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import org.checkerframework.checker.units.qual.A;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.foreign.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class PcapCleaningFilteringSplittingEvaluationServiceImpl extends EffectEvaluationService {
    @Resource
    private MinioMananger minioManager;

    @Override
    protected Map<String, Object> getResult(Commit commit) throws Exception{
        Map<String, Object> results = new HashMap<>();
        String[] path = commit.getPath().split("/");
        String bucketName = path[0];
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < path.length; i++) {
            sb.append(path[i]);
            if (i < path.length - 1) {
                sb.append("/");
            }
        }
        String zipName = sb.toString();
        InputStream zipInputStream = minioManager.getObject(bucketName, zipName);
        try(ZipInputStream zis = new ZipInputStream(zipInputStream, StandardCharsets.UTF_8)) {
            ZipEntry zipEntry;

            while ((zipEntry = zis.getNextEntry()) != null) {
                String[] fileNamePath = zipEntry.getName().split("/");
                String fileName = fileNamePath[fileNamePath.length - 1];
                // 只处理 .pcap 文件
                if (fileName.endsWith(".pcap")) {
                    // 将当前 zip 中的 pcap 文件读取为 InputStream
                    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                    byte[] buffer = new byte[1024];
                    int len;
                    while ((len = zis.read(buffer)) > 0) {
                        byteArrayOutputStream.write(buffer, 0, len);
                    }

                    // 创建 InputStream
                    InputStream pcapInputStream = new ByteArrayInputStream(byteArrayOutputStream.toByteArray());

                    // 计算 pcap 文件中数据包的数量
                    Integer packetCount = countPacketsInPcapFile(pcapInputStream);
                    results.put(fileName, packetCount);
                    // 关闭 InputStream
                    pcapInputStream.close();
                }
                zis.closeEntry();
            }
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return results;
    }
    @Override
    protected Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception{
        Map<String, Object> standards = new HashMap<>();
        String[] path = student2Resource.getCriterion().split("/");
        String bucketName = path[0];
        StringBuffer sb = new StringBuffer();
        for (int i = 1; i < path.length; i++) {
            sb.append(path[i]);
            sb.append("/");
        }
        String prefix = sb.toString();
        List<Item> pcapFiles = minioManager.getAllObjectsByPrefix(bucketName, prefix, false);
        for (Item item : pcapFiles) {
            String objectName = item.objectName();
            if (objectName.endsWith(".pcap")) {
                InputStream pcapInputStream = minioManager.getObject(bucketName, objectName);
                Integer packetCount = countPacketsInPcapFile(pcapInputStream);
                String[] pcapFilePath = objectName.split("/");
                String pcapFileName = pcapFilePath[pcapFilePath.length - 1];
                standards.put(pcapFileName, packetCount);
            }
        }
        return standards;
    }

    protected int countPacketsInPcapFile(InputStream pcapInputStream) throws IOException {
        // 创建临时文件
        File tempFile = File.createTempFile("tempPcap", ".pcap");
        // 确保程序结束时删除临时文件
        tempFile.deleteOnExit();

        // 将 InputStream 写入临时文件
        try (FileOutputStream out = new FileOutputStream(tempFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = pcapInputStream.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }

        // 使用数组来作为可变参数，在 lambda 表达式中访问
        final int[] packetCount = {0};

        // 定义原生回调处理程序
        PcapHandler.NativeCallback handler = (user, packet, header) -> {
            // 每处理一个数据包，增加计数
            packetCount[0]++;
        };

        try (Pcap pcap = Pcap.openOffline(tempFile)){
            // 开始解析数据包
            pcap.loop(-1, handler, MemorySegment.NULL);
        }
        catch(PcapException e) {
            return 0;
        }
        return packetCount[0];
    }

    @Override
    public CommitVO compare(Map<String, Object> results, Map<String, Object> standards){
        double scores = 0.00;
        List<String> mistakes = new ArrayList<>();
        StringBuffer sb = new StringBuffer();
        for(Map.Entry<String, Object> standard : standards.entrySet()) {
            String fileName = standard.getKey();
            Integer standardNum = (Integer) standard.getValue();
            Integer resultNum = (Integer) results.get(fileName);
            if(!standardNum.equals(resultNum)) {
                mistakes.add(fileName);
            }
            double score = resultNum == null ? 0.00 : (1.00 - Math.abs(standardNum - resultNum) / (double) standardNum) * 100;
            scores += score;
        }
        scores = scores / standards.size();
        if(mistakes.isEmpty()) {
            sb.append("恭喜你，全部提交正确！");
        }
        else {
            sb.append("提交有误的pcap文件：");
            for(int i = 0; i < mistakes.size() - 1; i++) {
                sb.append(mistakes.get(i));
                sb.append("; ");
            }
            sb.append(mistakes.getLast());
        }
        CommitVO commitVO = new CommitVO();
        commitVO.setScore(scores);
        commitVO.setRemark(sb.toString());
        return commitVO;
    }
}
