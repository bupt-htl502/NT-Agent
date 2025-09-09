package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PcapFilteringEvaluationServiceImplTest {
    @Resource(name = "pcapFilteringEvaluationServiceImpl")
    private EffectEvaluationService service;

    @Test
    void testReadPcap() throws Exception {
        Commit commit = new Commit();
        commit.setStudentId(10001L);
        commit.setSceneId(10004);
        commit.setPath("temporary/0.pcap");
        commit.setCreateTime(111L);
        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(10001L);
        student2Resource.setSceneId(10004);
        CommitVO commitVO = service.evaluate(student2Resource, commit);
        System.out.println(STR."score: \{commitVO.getScore()}");
        System.out.println(STR."remark: \{commitVO.getRemark()}");
    }
}
