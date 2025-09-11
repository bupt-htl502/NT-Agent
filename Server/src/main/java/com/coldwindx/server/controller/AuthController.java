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
import java.util.HashMap;
import java.util.Map;

@RestController
@UnifiedResponse
@RequestMapping("auth")
public class AuthController {
    @Resource
    private StudentController studentController;

    @RequestMapping(value = "role",method = RequestMethod.GET)
    public Map<String,String> role(@RequestParam String studentName, @RequestParam String studentNo){
        QueryParam<Student> queryParam = new QueryParam<>();
        queryParam.setCondition(new Student());
        queryParam.getCondition().setName(studentName);
        queryParam.getCondition().setStudentNo(studentNo);
        List<Student> students = studentController.query(queryParam);
        Map<String,String> map = new HashMap<>();
        if(students.isEmpty()){
            return null;
        }
        int role = students.getFirst().getRole();
        if(role==100){
            map.put("role","student");
            return map;
        }
        map.put("role","teacher");
        return map;
    }
}

