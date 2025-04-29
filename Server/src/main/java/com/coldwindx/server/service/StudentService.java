package com.coldwindx.server.service;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student;

import java.util.List;

public interface StudentService {
    List<Student> query(QueryParam<Student> params);
    Student insert(Student student);
    Student update(Student student);
    int delete(Student student);
}
