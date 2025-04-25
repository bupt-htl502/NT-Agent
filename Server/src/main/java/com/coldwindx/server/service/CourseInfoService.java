package com.coldwindx.server.service;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.CourseInfo;
import java.util.List;

public interface CourseInfoService {
    List<CourseInfo> resultQuery(QueryParam<CourseInfo> params);
}
