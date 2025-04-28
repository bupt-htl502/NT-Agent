package com.coldwindx.server.manager;

import io.github.imfangs.dify.client.DifyClient;
import io.github.imfangs.dify.client.callback.ChatStreamCallback;
import io.github.imfangs.dify.client.enums.ResponseMode;
import io.github.imfangs.dify.client.event.ErrorEvent;
import io.github.imfangs.dify.client.event.MessageEndEvent;
import io.github.imfangs.dify.client.event.MessageEvent;
import io.github.imfangs.dify.client.model.chat.ChatMessage;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import io.github.imfangs.dify.client.model.file.FileInfo;
import io.github.imfangs.dify.client.model.file.FileUploadRequest;
import io.github.imfangs.dify.client.model.file.FileUploadResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Slf4j
@Component
public class DifyManager {
    @Autowired
    private DifyClient client;

    public FileUploadResponse upload(String filename, InputStream stream) throws IOException {
        FileUploadRequest request = FileUploadRequest.builder().user("user-123").build();
        return client.uploadFile(request, stream, filename);
    }

    public ChatMessageResponse chat(String query, List<FileInfo> files) throws IOException {
        // 创建聊天消息
        ChatMessage message = ChatMessage.builder()
                .query(query)
                .user("user-123")
                .files(files)
                .responseMode(ResponseMode.BLOCKING)
                .build();

        // 发送消息并获取响应
        return client.sendChatMessage(message);
    }

    public void chat(String query, List<FileInfo> files, ChatStreamCallback callback) throws IOException {
        // 创建聊天消息
        ChatMessage message = ChatMessage.builder()
                .query(query)
                .files(files)
                .user("user-123")
                .responseMode(ResponseMode.STREAMING)
                .build();

        // 发送流式消息
        client.sendChatMessageStream(message, callback);
    }
}
