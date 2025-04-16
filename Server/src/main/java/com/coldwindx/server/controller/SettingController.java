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
    public List<Setting> query(@RequestBody QueryParam<Setting> param){
        return settingService.query(param);
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Setting insert(@RequestBody Setting setting){
        setting = settingService.insert(setting);
        return setting;
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Setting update(@RequestBody Setting setting){
        return settingService.update(setting);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Integer delete(@RequestBody Setting setting){
        return settingService.delete(setting);
    }

    @RequestMapping(value = "keys", method = RequestMethod.POST)
    public List<String> keys(){
        return settingService.keys();
    }
}
