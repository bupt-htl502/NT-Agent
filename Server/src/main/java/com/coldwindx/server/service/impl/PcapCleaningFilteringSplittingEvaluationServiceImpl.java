package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.*;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.lang.foreign.*;

public class PcapCleaningFilteringSplittingEvaluationServiceImpl extends EffectEvaluationService {
    @Override
    protected Map<String, Object> getResult(Commit commit) throws Exception{
        Map<String, Object> results = new HashMap<>();
        String path = commit.getPath();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".pcap"));
        for (File file : listOfFiles) {
            String fileName = file.getName();
            Integer packetCount = countPacketsInPcapFile(file.getAbsolutePath());
            results.put(fileName, packetCount);
        }
        return results;
    }
    @Override
    protected Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception{
        Map<String, Object> standards = new HashMap<>();
        String path = student2Resource.getCriterion();
        File folder = new File(path);
        File[] listOfFiles = folder.listFiles((dir, name) -> name.endsWith(".pcap"));
        for (File file : listOfFiles) {
            String fileName = file.getName();
            Integer packetCount = countPacketsInPcapFile(file.getAbsolutePath());
            standards.put(fileName, packetCount);
        }
        return standards;
    }

    protected int countPacketsInPcapFile(String pcapFilePath){
        // 使用数组来作为可变参数，在 lambda 表达式中访问
        final int[] packetCount = {0};

        // 定义原生回调处理程序
        PcapHandler.NativeCallback handler = (user, packet, header) -> {
            // 每处理一个数据包，增加计数
            packetCount[0]++;
        };

        try (Pcap pcap = Pcap.openOffline(pcapFilePath)){
            // 开始解析数据包
            pcap.loop(-1, handler, MemorySegment.NULL);
        }
        catch(PcapException e) {
            return 0;
        }
        return packetCount[0];
    }

    @Override
    public double compare(Map<String, Object> results, Map<String, Object> standards){
        double scores = 0.00;
        for(Map.Entry<String, Object> standard : standards.entrySet()) {
            String fileName = standard.getKey();
            Integer standardNum = (Integer) standard.getValue();
            Integer resultNum = (Integer) results.get(fileName);
            double score = resultNum == null ? 0.00 : (1.00 - Math.abs(standardNum - resultNum) / (double) standardNum) * 100;
            scores += score;
        }
        return scores / standards.size();
    }

}
