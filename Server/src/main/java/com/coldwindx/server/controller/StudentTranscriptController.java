package com.coldwindx.server.controller;


import com.alibaba.fastjson2.JSONObject;
import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.mapper.SettingMapper;
import com.coldwindx.server.mapper.StudentMapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@RestController
@UnifiedResponse
@RequestMapping("transcript")
public class StudentTranscriptController {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private CommitMapper commitMapper;
    @Autowired
    private SettingMapper settingMapper;
    @RequestMapping(value = "generate", method = RequestMethod.GET)
    public void download(@RequestParam(name = "studentId", required = true) long studentId, HttpServletResponse response) throws IOException {
//        鉴权
        QueryParam<Student> queryParam = new QueryParam<>();
        queryParam.setCondition(new Student());
        queryParam.getCondition().setId(studentId);
        List<Student> query = studentMapper.query(queryParam);
        if(query.getFirst().getRole() == 100){
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Permission denied");
        }
//        查所有学生及对应commit信息
        queryParam.setCondition(new Student());
        List<Student> allStudent = studentMapper.query(queryParam);
        Map<Student,Map<Integer,Double>> studentTranscriptMap = new HashMap<>();
        for(Student student:allStudent){
            if(student.getRole() == 200){
                continue;
            }
            QueryParam<Commit> param = new QueryParam<>();
            param.setCondition(new Commit());
            param.getCondition().setStudentId(student.getId());
            List<Commit> commitList = commitMapper.query(param);
            Map<Integer,Double> scoreMap = new HashMap<>();
            for(Commit commit:commitList){
                Double commitScore = commit.getScore();
                if(!scoreMap.containsKey(commit.getSceneId())){
                    scoreMap.put(commit.getSceneId(),commitScore);
                }
                else if(commitScore>scoreMap.get(commit.getSceneId())){
                    scoreMap.put(commit.getSceneId(),commitScore);
                }
            }
            studentTranscriptMap.put(student,scoreMap);
        }
//       查setting表获取全部子任务信息
        QueryParam<Setting> param1 = new QueryParam<>();
        param1.setCondition(new Setting());
        param1.getCondition().setKey("VUE_CONTENT_NODE");
        List<Setting> settingList = settingMapper.query(param1);
        Map<Integer,String> sceneIdMap = new TreeMap<>();
        for (Setting setting : settingList) {
            try {
                JSONObject root = JSONObject.parseObject(setting.getValue());
                if (root.getInteger("level")!=3) {
                    continue;
                }
                String [] tempS = root.getString("label").split("：");
                sceneIdMap.put(root.getInteger("id"),tempS[1]);
            } catch (Exception e) {
                // 可以打日志或跳过
            }
        }
//        设置响应头
        response.setContentType("text/csv;charset=UTF-8");
        String filename = URLEncoder.encode("学生成绩单.csv", StandardCharsets.UTF_8);
        response.setHeader("Content-Disposition", "attachment; filename*=UTF-8''" + filename);
//        写入csv
        try (PrintWriter writer = response.getWriter()) {
            // 写入标题
            writer.print("姓名");
            writer.print(",学号");
            for(String sceneName:sceneIdMap.values()){
                writer.print(","+sceneName);
            }
            writer.println();

            // 写入每一行
            for (Map.Entry<Student, Map<Integer, Double>> entry : studentTranscriptMap.entrySet()) {
                Student student = entry.getKey();
                Map<Integer, Double> scores = entry.getValue();

                writer.print(student.getName());
                writer.print(","+ student.getStudentNo());
                for (Integer sceneId : sceneIdMap.keySet()) {

                    writer.print("," + scores.getOrDefault(sceneId, 0.0));

                }
                writer.println();
            }
        }
    }

}


