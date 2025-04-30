package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.entity.form.Student;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface StudentMapper {
    List<Student> query(QueryParam<Student> params);
    int insert(Student student);
    int update(Student student);
    int delete(Student student);
}