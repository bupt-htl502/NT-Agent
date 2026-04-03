package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.SceneDto;
import com.coldwindx.server.entity.form.SceneInfo;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.mapper.SettingMapper;
import com.coldwindx.server.service.SettingService;
import com.alibaba.fastjson2.JSON;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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

    @Override
    public SceneInfo getSceneInfo(Integer sceneId) {
        QueryParam<Setting> queryParam = new QueryParam<>();
        Setting condition = new Setting();
        condition.setKey("VUE_CONTENT_NODE");
        condition.setIsdeleted(false);
        queryParam.setCondition(condition);
        List<Setting> settingList = query(queryParam);

        SceneInfo sceneInfo = new SceneInfo();
        sceneInfo.setSceneId(sceneId);
        Integer chapterId = null;
        Integer taskId = null;

        for (Setting setting : settingList) {
            String value = setting.getValue();
            SceneDto sceneDto = JSON.parseObject(value, SceneDto.class);
            if (sceneDto.getId().equals(sceneId)) {
                sceneInfo.setSceneName(sceneDto.getLabel());
                taskId = sceneDto.getParent();
                break;
            }
        }

        for (Setting setting : settingList) {
            String value = setting.getValue();
            SceneDto sceneDto = JSON.parseObject(value, SceneDto.class);
            if (sceneDto.getId().equals(taskId)) {
                sceneInfo.setTaskName(sceneDto.getLabel());
                chapterId = sceneDto.getParent();
                break;
            }
        }

        for (Setting setting : settingList) {
            String value = setting.getValue();
            SceneDto sceneDto = JSON.parseObject(value, SceneDto.class);
            if (sceneDto.getId().equals(chapterId)) {
                sceneInfo.setChapterName(sceneDto.getLabel());
                break;
            }
        }

        return sceneInfo;
    }

    @Override
    public List<SceneInfo> getSceneInfoList() {
        QueryParam<Setting> queryParam = new QueryParam<>();
        Setting condition = new Setting();
        condition.setKey("VUE_CONTENT_NODE");
        condition.setIsdeleted(false);
        queryParam.setCondition(condition);
        List<Setting> settingList = query(queryParam);

        List<SceneInfo> sceneInfoList = new ArrayList<>();
        for (Setting setting : settingList) {
            String value = setting.getValue();
            SceneDto sceneDto = JSON.parseObject(value, SceneDto.class);
            if (sceneDto.getLevel().equals(3)) {
                sceneInfoList.add(getSceneInfo(sceneDto.getId()));
            }
        }
        return sceneInfoList;
    }
}
