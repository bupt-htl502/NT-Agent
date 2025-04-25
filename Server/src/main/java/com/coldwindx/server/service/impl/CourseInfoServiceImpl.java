package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.CourseInfo;
import com.coldwindx.server.mapper.CourseInfoMapper;
import com.coldwindx.server.service.CourseInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseInfoServiceImpl implements CourseInfoService {
    @Autowired
    private CourseInfoMapper courseInfoMapper;

    @Override
    public List<CourseInfo> resultQuery(QueryParam<CourseInfo> params) {
        return courseInfoMapper.resultQuery(params);
    }
}
