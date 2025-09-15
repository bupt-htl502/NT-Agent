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

    @RequestMapping(value = "getScoreList", method = RequestMethod.GET)
    public StudentRecord getScoreList() {
        QueryParam<Student> queryParam = new QueryParam<>();
        Student condition = new Student();
        condition.setIsdeleted(false);
        queryParam.setCondition(condition);
        List<Student> students = studentService.query(queryParam);

        List<StudentScoreVo> studentList = commitService.getScoreList(students);

        List<SceneScoreVo> sceneAverages = commitService.getSceneAverage();

        int maxCommitTimes = 0;
        for (StudentScoreVo studentScore : studentList) {
            maxCommitTimes = Math.max(maxCommitTimes, studentScore.getCommitTimes());
        }

        StudentRecord studentRecord = new StudentRecord();
        studentRecord.setStudentList(studentList);
        studentRecord.setSceneAverages(sceneAverages);
        studentRecord.setMaxCommitTimes(maxCommitTimes);
        return studentRecord;
    }
}
