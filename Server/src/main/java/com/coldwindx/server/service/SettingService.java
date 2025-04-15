package com.coldwindx.server.service;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Setting;

import java.util.List;

public interface SettingService {
    List<Setting> query(QueryParam<Setting> params);

    Setting insert(Setting setting);
    Setting update(Setting setting);
    int delete(Setting setting);
}
