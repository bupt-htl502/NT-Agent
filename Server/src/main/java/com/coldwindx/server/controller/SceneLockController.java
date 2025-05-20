package com.coldwindx.server.controller;

import com.alibaba.fastjson2.JSONObject;
import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.mapper.StudentMapper;
import com.coldwindx.server.service.SettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@UnifiedResponse
@RequestMapping("lock")
public class SceneLockController {
    @Autowired
    CommitMapper commitMapper;

    @Autowired
    private SettingService settingService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public Boolean query(@RequestBody Commit commit) throws Exception {
//        1.根据传入的studentsid查询学生目前所应该完成的场景
        Long studentID = commit.getStudentId();
        Integer sceneID = commit.getSceneId();
        QueryParam<Setting> params = new QueryParam<>();
        params.condition = new Setting();
        params.condition.setKey("VUE_CONTENT_NODE");
        List<Setting> settings = settingService.query(params);
        String[] services = settings.stream().map(Setting::getValue)
                .map(JSONObject::parseObject)
                .filter(obj-> commit.getSceneId().equals(obj.getInteger("id")))
                .map(obj -> obj.getString("pre_exp"))
                .toArray(String[]::new);
//       2.查询该场景的上一个场景是否存在通过阈值的分数
        QueryParam<Commit> queryParam = new QueryParam<>();
        int newSceneId = Integer.parseInt(services[0]);
        Commit newCommit = new Commit();
        newCommit.setStudentId(studentID);
        newCommit.setSceneId(newSceneId);
        queryParam.setCondition(newCommit);
        List<Commit> commits = commitMapper.query(queryParam);
        for(int i = 0;i<10;i++){
            Double score = commits.get(i).getScore();
            if(score > 60.0){
                return true;
            }
        }
        return false;
    }

}
