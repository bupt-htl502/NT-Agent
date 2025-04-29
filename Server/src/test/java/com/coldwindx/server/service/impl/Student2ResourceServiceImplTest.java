package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.Student2ResourceService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
class Student2ResourceServiceImplTest {
    @Autowired
    private Student2ResourceService service;
    @Test
    void insertList() {
        List<Student2Resource> list = new ArrayList<>();
        Student2Resource o1 = new Student2Resource();
        o1.setStudentId(1L);
        o1.setSceneId(10000);
        o1.setPath("/home/path");
        o1.setCriterion("/home/criterion");
        o1.setCreateTime(0L);

        Student2Resource o2 = new Student2Resource();
        o2.setStudentId(1L);
        o2.setSceneId(10000);
        o2.setPath("/home/path");
        o2.setCriterion("/home/criterion");
        o2.setCreateTime(0L);

        list.add(o1);
        list.add(o2);

        service.insertList(list);
    }
}