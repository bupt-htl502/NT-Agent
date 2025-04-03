package com.coldwindx.server.controller;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("setting")
public class SettingController {
    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "test", method = RequestMethod.POST)
    public Object test(){
        QueryParam<Setting> param = new QueryParam<>();
        return settingService.query(param);
    }
}
