package com.coldwindx.server.controller;

import com.alibaba.fastjson2.JSONObject;
import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.LockResult;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.entity.form.Student;
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
    StudentMapper studentMapper;
    @Autowired
    SettingService settingService;
//旧版本查询，暂时废弃
//    @RequestMapping(value = "query", method = RequestMethod.POST)
//    public Boolean query(@RequestBody Commit commit) throws Exception {
////        1.根据传入的student查询学生目前所应该完成的场景
//        Long studentID = commit.getStudentId();
//        Integer sceneID = commit.getSceneId();
//        QueryParam<Setting> params = new QueryParam<>();
//        params.condition = new Setting();
//        params.condition.setKey("VUE_CONTENT_NODE");
//        List<Setting> settings = settingService.query(params);
////        解析id等于sceneID的场景信息，获取它上一个场景的id和通关分数
//        Double passScore = 0.0;
//        Integer newSceneId = 0;
//        for (Setting setting : settings) {
//            try {
//                JSONObject root = JSONObject.parseObject(setting.getValue());
//                if (!commit.getSceneId().equals(root.getInteger("id"))) {
//                    continue;
//                }
//                newSceneId = root.getInteger("pre_exp");
//                passScore = root.getDouble("pre_pass_score");
//                if(passScore==null){
//                    passScore = 60.0;
//                }
//                break; // 找到第一个匹配的就结束
//            } catch (Exception e) {
//                // 可以打日志或跳过
//            }
//        }
////       2.查询该场景的上一个场景是否存在通过阈值的分数
//        QueryParam<Commit> queryParam = new QueryParam<>();
//        if(newSceneId ==0) {
//            return true;
//        }else if(newSceneId==-1){
//            return false;
//        }
//        Commit newCommit = new Commit();
//        newCommit.setStudentId(studentID);
//        newCommit.setSceneId(newSceneId);
//        queryParam.setCondition(newCommit);
//        queryParam.setLimit(10);
//        List<Commit> commits = commitMapper.query(queryParam);
//        for (Commit value : commits) {
//            Double score = value.getScore();
    ////            此处的60.0可以使用配置文件中的参数进行替换
//            if (score > passScore) {
//                System.out.println("pass score:"+score);
//                return true;
//            }
//        }
//        System.out.println("not pass");
//        return false;
//    }
    @RequestMapping(value = "query", method = RequestMethod.POST)
    public LockResult query(@RequestBody Commit commit) throws Exception {
        LockResult lockResult = new LockResult();
        lockResult.message = "";
        lockResult.isLocked = true;
        //        查当前学生已经完成的场景号
        Student student = new Student();
        if(commit.getSceneId()==null){
            lockResult.isLocked = false;
            return lockResult;
        }
        student.setId(commit.getStudentId());
        QueryParam<Student> params = new QueryParam<>();
        params.condition = new Student();
        params.condition.setId(commit.getStudentId());

        List<Student> students = studentMapper.query(params);
        int nowScene = students.getFirst().getNowScene();
        //如果已完成场景号大于要去的场景号，直接通过
        if (nowScene >= commit.getSceneId()) {
//            System.out.println("ok,nowScene>=commitscene");
            lockResult.isLocked = false;
            return lockResult;
        }
//      如果是新账号，就默认返回第一个实验。
        if (nowScene == -1) {
//            System.out.println("no default value");
            lockResult.message = "场景1：Wireshark工具以及Tshark工具抓包";
            return lockResult;
        }
//      根据当前实验号查询应该前往的实验名
        QueryParam<Setting> paramsSetting = new QueryParam<>();
        paramsSetting.condition = new Setting();
        paramsSetting.condition.setKey("VUE_CONTENT_NODE");
        List<Setting> settings = settingService.query(paramsSetting);
        int nextSceneId = 0;
        for (Setting setting : settings) {
            try {
                JSONObject root = JSONObject.parseObject(setting.getValue());
                if (nowScene != root.getInteger("pre_exp")) {
                    continue;
                }
                nextSceneId = root.getInteger("id");
                lockResult.message = root.getString("label");
            } catch (Exception e) {
                // 可以打日志或跳过
            }
        }
        if (nextSceneId == commit.getSceneId()) {
//            System.out.println("ok,need to do this");
            lockResult.isLocked = false;
            return lockResult;
        }
//        System.out.println("no lockresult" + lockResult.message);
        return lockResult;
    }
}