package com.coldwindx.server.service;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Class;

import java.util.List;

public interface ClassService {
    List<Class> query(QueryParam<Class> params,int role);
    int insert(Class clazz,int tag);
    int update(Class clazz);
    int delete(Class clazz);
    Class queryByCode(String classCode);
    Class queryById(Long id);
}
