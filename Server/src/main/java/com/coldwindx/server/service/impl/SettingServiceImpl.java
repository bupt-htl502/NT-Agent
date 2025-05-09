package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.mapper.SettingMapper;
import com.coldwindx.server.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SettingServiceImpl implements SettingService {
    @Autowired
    private SettingMapper settingMapper;
    @Override
    public List<Setting> query(QueryParam<Setting> params) {
        return settingMapper.query(params);
    }

    @Override
    public Setting insert(Setting setting) {
        settingMapper.insert(setting);
        return setting;
    }

    @Override
    public Setting update(Setting setting) {
        settingMapper.update(setting);
        return setting;
    }

    @Override
    public int delete(Setting setting) {
        return settingMapper.delete(setting);
    }

    @Override
    public int count(QueryParam<Setting> params) {
        return settingMapper.count(params);
    }

    @Override
    public List<String> keys() {
        return settingMapper.keys();
    }
}
