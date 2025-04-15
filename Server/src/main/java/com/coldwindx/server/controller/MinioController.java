package com.coldwindx.server.controller;

import com.coldwindx.server.manager.MinioMananger;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequestMapping("minio")
public class MinioController {
    @Autowired
    private MinioMananger minioMananger;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file")MultipartFile file, @RequestParam(value = "bucket", required = false, defaultValue = "temporary") String bucket) throws Exception {
            return minioMananger.upload(file, bucket);
    }
}
