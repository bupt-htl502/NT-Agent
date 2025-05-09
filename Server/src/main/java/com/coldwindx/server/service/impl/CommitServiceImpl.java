package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.service.CommitService;
import com.coldwindx.server.service.EffectEvaluationService;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CommitServiceImpl implements CommitService {
    @Autowired
    private CommitMapper commitMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;


    @Override
    public List<Commit> query(QueryParam<Commit> params) {
        return commitMapper.query(params);
    }

    @Override
    public Commit insert(Commit commit) throws Exception {
        commitMapper.insert(commit);
        // 通知效果评估服务，执行评估
        // rabbitTemplate.convertAndSend("ex_student", "commited", commit);
        EffectEvaluationService service = new NumericalCharacteristicsEvaluationServiceImpl();
        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(commit.getStudentId());
        student2Resource.setSceneId(commit.getSceneId());
        double score = service.evaluate(student2Resource, commit);
        commit.setScore(score);
        return commit;
    }
}
