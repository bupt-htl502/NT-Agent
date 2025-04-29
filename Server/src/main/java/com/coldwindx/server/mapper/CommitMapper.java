package com.coldwindx.server.mapper;

import com.coldwindx.server.entity.form.Commit;
import org.springframework.stereotype.Component;

@Component
public interface CommitMapper {
    int insert(Commit commit);
}
