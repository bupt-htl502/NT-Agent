package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface CommitMapper {
    List<Commit> query(QueryParam<Commit> params);
    int insert(Commit commit);
}
