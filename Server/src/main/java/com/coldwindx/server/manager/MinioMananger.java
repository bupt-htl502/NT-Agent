package com.coldwindx.server.manager;

import io.minio.*;
import io.minio.errors.*;
import io.minio.messages.Bucket;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

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

    /**
     * 列出所有存储桶名称
     * @return List<Bucket>
     */
    public List<Bucket> listBuckets() throws Exception{
        return minioClient.listBuckets();
    }

    /**
     * 获取桶内一个对象
     * @param bucketName 桶名
     * @param objectName 对象路径（不包括桶名）
     * @return 对象流
     * @throws Exception
     */
    public InputStream getObject(String bucketName, String objectName) throws Exception {
        GetObjectArgs objectArgs = GetObjectArgs.builder().bucket(bucketName).object(objectName).build();
        return minioClient.getObject(objectArgs);
    }

    /**
     * 根据文件前置查询文件
     *
     * @param bucketName bucket名称
     * @param prefix     前缀
     * @param recursive  是否递归查询
     * @return MinioItem 列表
     */
    public List<Item> getAllObjectsByPrefix(String bucketName, String prefix, boolean recursive) throws Exception {

        ListObjectsArgs args = ListObjectsArgs.builder().bucket(bucketName).prefix(prefix).recursive(recursive).build();
        Iterable<Result<Item>> objectsIterator = minioClient.listObjects(args);

        List<Item> list = new ArrayList<>();
        for (Result<Item> o : objectsIterator) {
            list.add(o.get());
        }
        return list;
    }
}
