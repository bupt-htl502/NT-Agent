package com.coldwindx.server.manager;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@Component
public class MinioMananger {
    @Autowired
    private MinioClient minioClient;

    public String upload(MultipartFile file, String bucket) throws IOException, ServerException, InsufficientDataException, ErrorResponseException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException {
        // 1. 获取文件名
        log.info("文件{}准备上传至{}...", file.getOriginalFilename(), bucket);
        // 2. 文件上传
        InputStream stream = file.getInputStream();
        PutObjectArgs args = PutObjectArgs.builder().bucket(bucket).object(file.getOriginalFilename())
                .stream(stream, stream.available(), -1).contentType(file.getContentType()).build();
        minioClient.putObject(args);
        // 3. 文件上传成功
        return "File upload successfully!";

    }
}
