package com.coldwindx.server.service;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.entity.form.Student2Resource;

import java.util.List;

public interface Student2ResourceService {
    List<Student2Resource> query(QueryParam<Student2Resource> params);
    Student2Resource insert(Student2Resource student2resource);
    Student2Resource update(Student2Resource student2resource);
    int delete(Student2Resource student2resource);
    void insertList(List<Student2Resource> list);
}
