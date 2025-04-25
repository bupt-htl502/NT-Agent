package com.coldwindx.server.service;

import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface DifyService {
    ChatMessageResponse chat(String query, MultipartFile pcap) throws IOException;
}
