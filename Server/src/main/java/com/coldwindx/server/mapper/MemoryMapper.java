package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.form.Memory;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface MemoryMapper {
    int insert(Memory memory);
}
