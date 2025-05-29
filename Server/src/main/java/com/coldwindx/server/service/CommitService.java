package com.coldwindx.server.service;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;

import java.util.List;

public interface CommitService {
    List<Commit> query(QueryParam<Commit> params);
    CommitVO insert(Commit commit) throws Exception;
}
