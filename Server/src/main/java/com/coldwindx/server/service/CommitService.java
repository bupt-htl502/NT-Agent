package com.coldwindx.server.service;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.AverageVo;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.SceneScoreVo;
import com.coldwindx.server.entity.StudentScoreVo;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student;

import java.util.List;

public interface CommitService {
    List<Commit> query(QueryParam<Commit> params);
    CommitVO insert(Commit commit) throws Exception;
    AverageVo getScoreList(List<Student> students);
    List<SceneScoreVo> getSceneAverage();
}
