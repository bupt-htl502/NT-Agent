package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.mapper.StudentMapper;
import com.coldwindx.server.service.StudentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentServiceImpl implements StudentService {
    @Autowired
    private StudentMapper studentMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public List<Student> query(QueryParam<Student> params) {
        return studentMapper.query(params);
    }

    @Override
    public Student insert(Student student) {
        studentMapper.insert(student);
        // 发送消息通知，构造学生-资源映射表
        rabbitTemplate.convertAndSend("ex_student", "registed_test", student);
        return student;
    }

    @Override
    public Student update(Student student) {
        studentMapper.update(student);
        return student;
    }

    @Override
    public int delete(Student student) {
        return studentMapper.delete(student);
    }
}