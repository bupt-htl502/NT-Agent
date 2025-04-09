package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Memory;
import com.coldwindx.server.mapper.MemoryMapper;
import com.coldwindx.server.service.MemoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MemoryServiceImpl implements MemoryService {
    @Autowired
    private MemoryMapper memoryMapper;

    @Override
    public Memory insert(Memory memory) {
        memoryMapper.insert(memory);
        return memory;
    }
}
