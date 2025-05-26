package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@UnifiedResponse
@RequestMapping("student")
public class StudentController {
    @Autowired
    private StudentService studentService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public List<Student> query(@RequestBody QueryParam<Student> param) {
        return studentService.query(param);
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Student insert(@RequestBody Student student) {
        return studentService.insert(student);
    }
    @RequestMapping(value = "testModeInsert",method = RequestMethod.POST)
    public Student testModeInsert(@RequestBody Student student){
        return studentService.testModeInsert(student);
    }
    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Student update(@RequestBody Student student) {
        return studentService.update(student);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Integer delete(@RequestBody Student student) {
        return studentService.delete(student);
    }
}
