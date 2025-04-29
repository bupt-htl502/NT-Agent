package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.service.CommitService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommitServiceImpl implements CommitService {
    @Autowired
    private CommitMapper commitMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public Commit insert(Commit commit) {
        commitMapper.insert(commit);
        // 通知效果评估服务，执行评估
        rabbitTemplate.convertAndSend("ex_student", "commited", commit);
        return commit;
    }
}
