package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Setting;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface SettingMapper {
    List<Setting> query(QueryParam<Setting> params);
    int insert(Setting setting);
    int update(Setting setting);
    int delete(Setting setting);
    int count(QueryParam<Setting> params);

    List<String> keys();
}
