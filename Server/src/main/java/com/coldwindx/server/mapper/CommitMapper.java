package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommitMapper {
    int insert(Commit commit);
    List<Commit> query(QueryParam<Commit> params);
    void update(Commit commit);
}
