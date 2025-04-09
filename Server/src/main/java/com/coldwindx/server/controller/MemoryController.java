package com.coldwindx.server.controller;

import com.coldwindx.server.entity.form.Memory;
import com.coldwindx.server.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("memory")
public class MemoryController {
    @Autowired
    private MemoryService memoryService;
    @RequestMapping(value = "insert", method = RequestMethod.POST)
    public Memory insert(@RequestBody Memory memory){
        return memoryService.insert(memory);
    }
}
