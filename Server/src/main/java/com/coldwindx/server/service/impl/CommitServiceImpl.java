package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.service.CommitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitServiceImpl implements CommitService {
    @Autowired
    private CommitMapper commitMapper;

    @Override
    public List<Commit> query(QueryParam<Commit> params) {
        return commitMapper.query(params);
    }

    @Override
    public Commit insert(Commit commit) {
        commitMapper.insert(commit);
        return commit;
    }
}
