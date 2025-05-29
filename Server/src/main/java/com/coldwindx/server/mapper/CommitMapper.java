package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

import javax.management.MXBean;
import java.util.List;

@Mapper
public interface CommitMapper {
    int insert(Commit commit);
    List<Commit> query(QueryParam<Commit> params);
    void update(Commit commit);
}
