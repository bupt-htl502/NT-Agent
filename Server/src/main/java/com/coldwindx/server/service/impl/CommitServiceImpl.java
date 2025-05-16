package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.service.CommitService;
import com.coldwindx.server.service.EffectEvaluationService;
import jakarta.annotation.Resource;
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
    @Resource(name = "numericalCharacteristicsEvaluationServiceImpl")
    private EffectEvaluationService numericalService;

    @Resource(name = "stringFeaturesEvaluationServiceImpl")
    private EffectEvaluationService stringService;

    @Override
    public List<Commit> query(QueryParam<Commit> params) {
        return commitMapper.query(params);
    }

    @Override
    public CommitVO insert(Commit commit) throws Exception {
        commitMapper.insert(commit);
        // 通知效果评估服务，执行评估
        // rabbitTemplate.convertAndSend("ex_student", "commited", commit);
        //    @Resource(name = "numericalCharacteristicsEvaluationServiceImpl")
        EffectEvaluationService service;

//        if(commit.getSceneId() == 20002 || commit.getSceneId() == 20005 || commit.getSceneId() == 20006) {
//            service = new PcapCleaningFilteringSplittingEvaluationServiceImpl();
//        }
//        else if(commit.getSceneId() == 20003) {
//            service = new PcapSortingEvaluationServiceImpl();
//        }
        if(commit.getSceneId() == 40002 || commit.getSceneId() == 40003 || commit.getSceneId() == 40004 || commit.getSceneId() == 40011) {
            service = numericalService;
        }
        else {
            service = stringService;
        }
        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(commit.getStudentId());
        student2Resource.setSceneId(commit.getSceneId());
        return service.evaluate(student2Resource, commit);
    }
}
