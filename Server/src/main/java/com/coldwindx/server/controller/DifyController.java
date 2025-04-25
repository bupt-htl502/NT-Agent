package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.service.DifyService;
import io.github.imfangs.dify.client.model.chat.ChatMessageResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@UnifiedResponse
@RestController
@RequestMapping("dify")
public class DifyController {
    @Autowired
    private DifyService service;

    @RequestMapping(value = "chat", method = RequestMethod.POST)
    public ChatMessageResponse chat(@RequestParam("file") MultipartFile file, @RequestParam(value = "query") String query) throws IOException {
        return service.chat(query, file);
    }
}
