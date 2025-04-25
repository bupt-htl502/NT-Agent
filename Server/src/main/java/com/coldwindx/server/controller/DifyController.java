package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.NtAgentException;
import com.coldwindx.server.entity.enums.ResponseCode;
import com.coldwindx.server.service.DifyService;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import io.github.imfangs.dify.client.model.file.FileUploadResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@UnifiedResponse
@RestController
@RequestMapping("dify")
public class DifyController {
    @Autowired
    private DifyService service;

    @RequestMapping(value = "upload", method = RequestMethod.POST)
    public FileUploadResponse upload(@RequestParam("file") MultipartFile pcap) throws IOException {
        return service.upload(pcap);
    }

    @RequestMapping(value = "chat", method = RequestMethod.POST)
    public ChatMessageResponse chat(@RequestBody Map<String, String> params) throws IOException {
        String fileid = params.getOrDefault("fileid", "");
        String query = params.getOrDefault("query", "");
        if(StringUtils.isBlank(fileid))
            throw new NtAgentException(ResponseCode.INVALID_PARAMETERS.getCode(), "参数非法：`fileid`不能为空！");
        if(StringUtils.isBlank(query))
            throw new NtAgentException(ResponseCode.INVALID_PARAMETERS.getCode(), "参数非法：`query`不能为空！");
        return service.chat(query, fileid);
    }
}
