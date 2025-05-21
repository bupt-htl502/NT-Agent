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
//        1.根据传入的student查询学生目前所应该完成的场景
        Long studentID = commit.getStudentId();
        Integer sceneID = commit.getSceneId();
        QueryParam<Setting> params = new QueryParam<>();
        params.condition = new Setting();
        params.condition.setKey("VUE_CONTENT_NODE");
        List<Setting> settings = settingService.query(params);
//        解析id等于sceneID的场景信息，获取它上一个场景的id和通关分数
        Double passScore = 0.0;
        Integer newSceneId = 0;
        for (Setting setting : settings) {
            try {
                JSONObject root = JSONObject.parseObject(setting.getValue());
                if (!commit.getSceneId().equals(root.getInteger("id"))) {
                    continue;
                }
                newSceneId = root.getInteger("pre_exp");
                passScore = root.getDouble("pre_pass_score");
                if(passScore==null){
                    passScore = 60.0;
                }
                break; // 找到第一个匹配的就结束
            } catch (Exception e) {
                // 可以打日志或跳过
            }
        }
//       2.查询该场景的上一个场景是否存在通过阈值的分数
        QueryParam<Commit> queryParam = new QueryParam<>();
        if(newSceneId ==0) {
            return true;
        }else if(newSceneId==-1){
            return false;
        }
        Commit newCommit = new Commit();
        newCommit.setStudentId(studentID);
        newCommit.setSceneId(newSceneId);
        queryParam.setCondition(newCommit);
        queryParam.setLimit(10);
        List<Commit> commits = commitMapper.query(queryParam);
        for (Commit value : commits) {
            Double score = value.getScore();
//            此处的60.0可以使用配置文件中的参数进行替换
            if (score > passScore) {
                System.out.println("pass score:"+score);
                return true;
            }
        }
        System.out.println("not pass");
        return false;
    }
}
