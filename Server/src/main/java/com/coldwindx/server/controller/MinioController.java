package com.coldwindx.server.controller;

import com.alibaba.fastjson2.JSONObject;
import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.NtAgentException;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.enums.ResponseCode;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.SettingService;
import io.minio.messages.Bucket;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Slf4j
@RestController
@UnifiedResponse
@RequestMapping("minio")
public class MinioController {
    @Autowired
    private MinioMananger minioMananger;
    @Autowired
    private SettingService settingService;
    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public String upload(@RequestParam("file")MultipartFile file, @RequestParam(value = "bucket", required = false, defaultValue = "studentsdata") String bucket, @RequestParam(value = "path", required = false, defaultValue = "")String path) throws Exception {
            return minioMananger.upload(file, bucket, path);
    }

    /**
     * 下载文件 根据文件名
     *
     * @param bucket   Minio的桶名，必填项
     * @param path     Minio的文件路径名（不包括桶名），必填项
     * @param response Http响应体
     */
    @RequestMapping(value = "download", method = RequestMethod.GET)
    public void download(@RequestParam(name = "bucket", required = true) String bucket, @RequestParam(name = "path", required = true) String path, HttpServletResponse response) throws Exception{
        InputStream inputStream = null;
        OutputStream outputStream = null;
        if (StringUtils.isBlank(path))
            throw new NtAgentException(ResponseCode.INVALID_PARAMETERS);
        outputStream = response.getOutputStream();
        // 获取文件对象
        inputStream = minioMananger.getObject(bucket, path);
        byte[] buf = new byte[1024];
        int length = 0;
        response.reset();
        String originalFileName = path.substring(path.lastIndexOf("/") + 1);
        String[] p = path.split("/");
        String sceneId = p[p.length - 2];
        String fileExtension = "";
        int dotIndex = originalFileName.lastIndexOf(".");
        if (dotIndex != -1) {
            fileExtension = originalFileName.substring(dotIndex); // 保留原始扩展名，如 .zip, .csv
        }

        QueryParam<Setting> paramsSetting = new QueryParam<>();
        paramsSetting.condition = new Setting();
        paramsSetting.condition.setKey("VUE_CONTENT_NODE");
        List<Setting> settings = settingService.query(paramsSetting);
        String sceneName = null;
        for (Setting setting : settings) {
            try {
                JSONObject root = JSONObject.parseObject(setting.getValue());
                if (!sceneId.equals(root.getString("id"))) {
                    continue;
                }
                sceneName = root.getString("label");
            } catch (Exception e) {
                // 可以打日志或跳过
            }
        }
        String obfuscatedFileName = sceneName+ "experiment_data" + fileExtension;

        response.setHeader("Content-Disposition", "attachment;filename=" +
                URLEncoder.encode(obfuscatedFileName, StandardCharsets.UTF_8));

        response.setContentType("application/octet-stream");
        response.setCharacterEncoding("UTF-8");
        // 输出文件
        while ((length = inputStream.read(buf)) > 0) {
            outputStream.write(buf, 0, length);
        }
        inputStream.close();
    }

    @RequestMapping(value = "listBucketNames", method = RequestMethod.POST)
    public List<String> listBucketNames() throws Exception {
        List<Bucket> buckets = minioMananger.listBuckets();
        return buckets.stream().map(Bucket::name).toList();
    }
}
