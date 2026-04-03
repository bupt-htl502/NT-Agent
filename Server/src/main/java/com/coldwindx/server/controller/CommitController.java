package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.*;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.service.CommitService;
import com.coldwindx.server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@UnifiedResponse
@RequestMapping("commit")
public class CommitController {
    @Autowired
    private CommitService commitService;
    @Autowired
    private StudentService studentService;


    @RequestMapping(value = "query", method = RequestMethod.POST)
    public List<Commit> query(@RequestBody QueryParam<Commit> param) {
        return commitService.query(param);
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public CommitVO insert(@RequestBody Commit commit) throws Exception {
        return commitService.insert(commit);
    }
    // 修改为按照班级查询学生成绩
    @RequestMapping(value = "getScoreList", method = RequestMethod.POST)
    public StudentRecord getScoreList(@RequestBody Map<String, Object> requestBody) {
        Long classId = null;
        Object classIdObj = requestBody.get("classId");
        if (classIdObj instanceof Integer) {
            classId = ((Integer) classIdObj).longValue();
        } else if (classIdObj instanceof Long) {
            classId = (Long) classIdObj;
        }
        // 检查classId是否为null
        if (classId == null) {
            // 可以返回一个错误响应，或者设置默认值
            return null;
        }

        QueryParam<Student> queryParam = new QueryParam<>();
        Student condition = new Student();
        condition.setIsdeleted(false);
        condition.setRole(100);
        if(classId!=100000){
            condition.setClassId(classId);
        }
        queryParam.setCondition(condition);
        List<Student> students = studentService.query(queryParam);

        AverageVo averageVo = commitService.getScoreList(students);

        // List<SceneScoreVo> sceneAverages = commitService.getSceneAverage();
        List<SceneScoreVo> sceneAverages = averageVo.getSceneScore();

        List<StudentScoreVo> studentList = averageVo.getStudentScore();

        int maxCommitTimes = 0;
        for (StudentScoreVo studentScore : studentList) {
            maxCommitTimes = Math.max(maxCommitTimes, studentScore.getSumCommitTimes());
        }


        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setStudentList(studentList);
        studentRecord.setSceneAverages(sceneAverages);
        StudentRecord.ScoreStatisticDto statistics = new StudentRecord.ScoreStatisticDto();
        statistics.setMaxCommitTimes(maxCommitTimes);
        studentRecord.setStatistics(statistics);
        return studentRecord;
    }
}
