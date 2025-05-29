package com.coldwindx.server.service.impl;

import com.coldwindx.server.controller.SceneLockController;
import com.coldwindx.server.entity.form.Commit;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class SceneLockControllerTest {
    @Autowired
    SceneLockController sceneLockController;

    @Test
    void testLock() throws Exception{
        Commit commit = new Commit();
        commit.setSceneId(20006);
        commit.setStudentId(154L);
        sceneLockController.query(commit);
    }
}
