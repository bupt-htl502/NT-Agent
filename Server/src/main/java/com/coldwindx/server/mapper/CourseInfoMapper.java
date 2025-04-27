package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.CourseInfo;
import org.apache.ibatis.annotations.Mapper;
import java.util.List;

@Mapper
public interface CourseInfoMapper {
    List<CourseInfo> resultQuery(QueryParam<CourseInfo> params);
}
