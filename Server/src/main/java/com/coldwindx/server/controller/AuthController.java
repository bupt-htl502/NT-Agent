package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@UnifiedResponse
@RequestMapping("auth")
public class AuthController {
    @Resource
    private StudentController studentController;

    @RequestMapping(value = "role",method = RequestMethod.GET)
    public Map<String, String> role(@RequestParam String studentName,@RequestParam String studentNo){
        QueryParam<Student> queryParam = new QueryParam<>();
        queryParam.setCondition(new Student());
        queryParam.getCondition().setName(studentName);
        queryParam.getCondition().setStudentNo(studentNo);
        List<Student> students = studentController.query(queryParam);
        if(students.isEmpty()){
            return " ";
        }
        int role = students.getFirst().getRole();
        if(role==100){
            return Map.of("role", "student");
        }
        return Map.of("role", "teacher");
    }
}
