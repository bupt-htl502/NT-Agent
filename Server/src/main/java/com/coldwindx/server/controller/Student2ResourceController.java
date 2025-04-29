package com.coldwindx.server.controller;

import com.coldwindx.server.aop.UnifiedResponse;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.Student2ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@UnifiedResponse
@RequestMapping("student2resource")
public class Student2ResourceController {
    @Autowired
    private Student2ResourceService student2resourceService;

    @RequestMapping(value = "query", method = RequestMethod.POST)
    public List<Student2Resource> query(@RequestBody QueryParam<Student2Resource> param) {
        return student2resourceService.query(param);
    }

    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Student2Resource insert(@RequestBody Student2Resource student2resource) {
        return student2resourceService.insert(student2resource);
    }

    @RequestMapping(value = "update", method = RequestMethod.POST)
    public Student2Resource update(@RequestBody Student2Resource student2resource) {
        return student2resourceService.update(student2resource);
    }

    @RequestMapping(value = "delete", method = RequestMethod.POST)
    public Integer delete(@RequestBody Student2Resource student2resource) {
        return student2resourceService.delete(student2resource);
    }
}
