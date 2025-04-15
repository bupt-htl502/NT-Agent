package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@UnifiedResponse
@RequestMapping("setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public List<Setting> query(){
        QueryParam<Setting> param = new QueryParam<>();
        return settingService.query(param);
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Setting insert(@RequestBody Setting setting){
        setting = settingService.insert(setting);
        return setting;
    }
}
