package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.StudentRegistrationMessage;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.mapper.StudentMapper;
import com.coldwindx.server.service.StudentService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
        StudentRegistrationMessage studentRegistrationMessage = new StudentRegistrationMessage();
        studentRegistrationMessage.student = student;
        studentRegistrationMessage.isTestMode = false;
        rabbitTemplate.convertAndSend("ex_student", "registed_test", studentRegistrationMessage);
        return student;
    }
    @Override
    public Student testModeInsert(Student student){
        studentMapper.insert(student);
        StudentRegistrationMessage studentRegistrationMessage = new StudentRegistrationMessage();
        studentRegistrationMessage.student = student;
        studentRegistrationMessage.isTestMode = true;
        rabbitTemplate.convertAndSend("ex_student", "registed_test", studentRegistrationMessage);
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

    @Override
    @Transactional
    public Student queryAndInsert(String name,String studentNo,String role) {
        QueryParam<Student> params = new QueryParam<>();
        params.setCondition(new Student());
        params.getCondition().setName(name);
        params.getCondition().setStudentNo(studentNo);

       List<Student> students = studentMapper.query(params);
       if(students.isEmpty()){
           Student student = new Student();
           student.setStudentNo(studentNo);
           int roleCode = checkStuOrTeacher(studentNo);
           student.setRole(roleCode);
           student.setName(name);
           student.setNowScene(40012);
           student.setIsdeleted(false);
           student.setGrade(0);
           insert(student);
           return student;
       }
       return students.getFirst();
    }

    public int checkStuOrTeacher(String role){
        if(role.equals("L0101")||role.equals("L0107")){
            return 200;
        }
        return 100;
    }


}