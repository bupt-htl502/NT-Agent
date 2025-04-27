package com.coldwindx.server.service.impl;

import com.coldwindx.server.manager.DifyManager;
import com.coldwindx.server.service.DifyService;
import io.github.imfangs.dify.client.callback.ChatStreamCallback;
import io.github.imfangs.dify.client.enums.FileTransferMethod;
import io.github.imfangs.dify.client.enums.FileType;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import io.github.imfangs.dify.client.model.file.FileInfo;
import io.github.imfangs.dify.client.model.file.FileUploadResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.List;

@Service
public class DifyServiceImpl implements DifyService {
    @Autowired
    private DifyManager manager;

    @Override
    public FileUploadResponse upload(MultipartFile pcap) throws IOException {
        String filename = pcap.getOriginalFilename();
        InputStream stream = pcap.getInputStream();
        return manager.upload(filename, stream);
    }

    @Override
    public ChatMessageResponse chat(String query, String fileid) throws IOException {
        // 2. 创建文件信息
        FileInfo fileInfo = FileInfo.builder()
                .type(FileType.CUSTOM)                              // 这里类型需要与Dify文件上传类型保持一致
                .transferMethod(FileTransferMethod.LOCAL_FILE)
                .uploadFileId(fileid)
                .build();
        List<FileInfo> files = Collections.singletonList(fileInfo);
        // 3. run chat flow
        return manager.chat(query, files);
    }

    @Override
    public void chat(String query, String fileid, ChatStreamCallback callback) throws IOException {
        // 2. 创建文件信息
        FileInfo fileInfo = FileInfo.builder()
                .type(FileType.CUSTOM)                              // 这里类型需要与Dify文件上传类型保持一致
                .transferMethod(FileTransferMethod.LOCAL_FILE)
                .uploadFileId(fileid)
                .build();
        List<FileInfo> files = Collections.singletonList(fileInfo);
        // 3. run chat flow
        manager.chat(query, files, callback);
    }


}
