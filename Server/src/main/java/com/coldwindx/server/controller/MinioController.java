package com.coldwindx.server.controller;

import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import io.minio.errors.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@Slf4j
@RestController
@RequestMapping("minio")
public class MinioController {
    @Autowired
    private MinioClient minioClient;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public Object upload(@RequestParam("file")MultipartFile file, @RequestParam("bucket") String bucket){
        try {
            InputStream stream = file.getInputStream();
            PutObjectArgs args = PutObjectArgs.builder().bucket(bucket).object(file.getOriginalFilename())
                    .stream(stream, stream.available(), -1).contentType(file.getContentType()).build();
            minioClient.putObject(args);
            return "File upload successfully!";
        } catch (MinioException | IOException | NoSuchAlgorithmException | InvalidKeyException e) {
            log.error(e.getMessage(), e);
            return "Error uploading file to MinIO: " + e.getMessage();
        }
    }
}
