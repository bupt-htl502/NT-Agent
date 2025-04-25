package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.NtAgentException;
import com.coldwindx.server.entity.enums.ResponseCode;
import com.coldwindx.server.entity.form.FeishuWebComponentAuth;
import com.coldwindx.server.manager.FeishuManager;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@UnifiedResponse
@RestController
@RequestMapping(value = "feishu")
public class FeishuController {
    @Autowired
    private FeishuManager manager;

    @RequestMapping(value = "signature", method = RequestMethod.POST)
    public FeishuWebComponentAuth signature(@RequestBody Map<String, String> params){
        String url = params.getOrDefault("url", "");
        if(StringUtils.isBlank(url))
                throw new NtAgentException(ResponseCode.INVALID_PARAMETERS.getCode(), "无效的参数：`url`必须是合法的链接！");
        return manager.signature(url);
    }
}
