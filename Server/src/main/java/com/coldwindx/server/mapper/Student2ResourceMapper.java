package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student2Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface Student2ResourceMapper {
    List<Student2Resource> query(QueryParam<Student2Resource> params);
    int insert(Student2Resource student2Resource);
    int update(Student2Resource student2Resource);
    int delete(Student2Resource student2Resource);

    void insertList(List<Student2Resource> list);
}
