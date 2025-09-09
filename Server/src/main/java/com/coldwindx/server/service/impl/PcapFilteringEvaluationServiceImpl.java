package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.EffectEvaluationService;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import org.jnetpcap.Pcap;
import org.jnetpcap.PcapHandler;
import org.jnetpcap.PcapHeader;
import org.jnetpcap.*;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.lang.foreign.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Service
public class PcapFilteringEvaluationServiceImpl extends EffectEvaluationService {
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
        String objectName = sb.toString();
        InputStream pcapInputStream = minioManager.getObject(bucketName, objectName);
        if (objectName.endsWith(".pcap")) {
            // 计算 pcap 文件中数据包的数量
            int[] packetCount = countTcpPacketsInPcapFile(pcapInputStream);
            results.put("packetCount", packetCount[1]);
            results.put("tcpPacketCount", packetCount[0]);
            // 关闭 InputStream
            pcapInputStream.close();
        }
        return results;
    }
    @Override
    protected Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception{
        return null;
    }

    protected int[] countTcpPacketsInPcapFile(InputStream pcapInputStream) throws IOException {
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
        final int[] packetCount = {0, 0};

        // 定义原生回调处理程序
        PcapHandler.NativeCallback handler1 = (user, packet, header) -> {
            // 每处理一个数据包，增加计数
            packetCount[0]++;
        };
        PcapHandler.NativeCallback handler2 = (user, packet, header) -> {
            // 每处理一个数据包，增加计数
            packetCount[1]++;
        };

        try (Pcap pcap = Pcap.openOffline(tempFile)){
            // 设置过滤器，过滤tcp协议的数据包
            BpFilter filter = pcap.compile("tcp", true);
            pcap.setFilter(filter);
            // 开始解析数据包
            pcap.loop(-1, handler1, MemorySegment.NULL);
        }
        catch(PcapException e) {
            return null;
        }
        try (Pcap pcap = Pcap.openOffline(tempFile)){
            // 开始解析数据包
            pcap.loop(-1, handler2, MemorySegment.NULL);
        }
        catch(PcapException e) {
            return null;
        }
        return packetCount;
    }

    public CommitVO compare(Map<String, Object> results, Map<String, Object> standards){
        double scores = 0.00;
        StringBuffer sb = new StringBuffer();
        Integer packetCount = (Integer) results.get("packetCount");
        Integer tcpPacketCount = (Integer) results.get("tcpPacketCount");
        scores = (double) tcpPacketCount / packetCount * 100;
        if(packetCount.equals(tcpPacketCount)) {
            sb.append("恭喜你，筛选正确");
        }
        else {
            sb.append("筛选有误，请检查你的pcap文件");
        }
        CommitVO commitVO = new CommitVO();
        commitVO.setScore(scores);
        commitVO.setRemark(sb.toString());
        return commitVO;
    }
}
