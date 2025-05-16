package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.form.Memory;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.mapper.StudentMapper;
import com.coldwindx.server.service.impl.StudentServiceImpl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@UnifiedResponse
@RequestMapping("auth")
public class AuthController {
    @RequestMapping(value = "register", method = RequestMethod.POST)
    public String register(@RequestBody Map<String, String> params) throws InterruptedException {
        Student student = new Student();
        StudentServiceImpl studentService = new StudentServiceImpl();
        student.setName(params.get("name"));
        student.setStudentNo(params.get("studentNo"));
        student.setRole(100);
        studentService.insert(student);
        return "注册成功";
    }

}
