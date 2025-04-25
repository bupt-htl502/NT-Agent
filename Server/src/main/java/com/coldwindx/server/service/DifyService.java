package com.coldwindx.server.service;

import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import io.github.imfangs.dify.client.model.file.FileUploadResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DifyService {
    FileUploadResponse upload(MultipartFile pcap) throws IOException;
    ChatMessageResponse chat(String query, String fileid) throws IOException;
}
