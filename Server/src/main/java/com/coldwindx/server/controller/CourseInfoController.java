package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.CourseInfo;
import com.coldwindx.server.service.CourseInfoService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Slf4j
@RestController
@UnifiedResponse
@RequestMapping("courseinfo")
public class CourseInfoController {
    @Autowired
    private CourseInfoService courseInfoService;


    @RequestMapping(value = "resultQuery", method = RequestMethod.POST)
    public List<CourseInfo> resultQuery(@RequestBody QueryParam<CourseInfo> param ) {
        return courseInfoService.resultQuery(param);
    }

}
