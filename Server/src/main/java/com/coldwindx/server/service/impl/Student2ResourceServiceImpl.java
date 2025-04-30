package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.Student2ResourceMapper;
import com.coldwindx.server.service.Student2ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class Student2ResourceServiceImpl implements Student2ResourceService {
    @Autowired
    private Student2ResourceMapper student2resourceMapper;
    @Override
    public List<Student2Resource> query(QueryParam<Student2Resource> params) {
        return student2resourceMapper.query(params);
    }

    @Override
    public Student2Resource insert(Student2Resource student2resource) {
        student2resourceMapper.insert(student2resource);
        return student2resource;
    }

    @Override
    public Student2Resource update(Student2Resource student2resource) {
        student2resourceMapper.update(student2resource);
        return student2resource;
    }

    @Override
    public int delete(Student2Resource student2resource) {
        return student2resourceMapper.delete(student2resource);
    }

    @Override
    public void batchInsert(List<Student2Resource> list) {
        student2resourceMapper.batchInsert(list);
    }
}
