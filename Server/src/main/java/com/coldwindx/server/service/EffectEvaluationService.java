package com.coldwindx.server.service;

import com.alibaba.fastjson2.JSONObject;
import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.mapper.SettingMapper;
import com.coldwindx.server.mapper.Student2ResourceMapper;
import com.coldwindx.server.mapper.StudentMapper;
import com.coldwindx.server.service.impl.StudentServiceImpl;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Stream;

public abstract class EffectEvaluationService {
    @Resource
    private Student2ResourceMapper student2ResourceMapper;
    @Autowired
    private CommitMapper commitMapper;
    @Autowired
    private StudentService studentService;
    /**
     * 效果评估服务
     *
     * @param results   用户结果
     * @param standards 标准结果
     * @return 效果评估结果分数
     */
    public abstract CommitVO compare(Map<String, Object> results, Map<String, Object> standards);

    protected abstract Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception;

    protected abstract Map<String, Object> getResult(Commit commit) throws Exception;

    protected void afterCompare(double score, Commit commit) throws Exception {
        commit.setScore(score);
        if(score > 60.0){
            Student student = new Student();
            student.setId(commit.getStudentId());
            student.setNowScene(commit.getSceneId());
            studentService.update(student);
        }
        commitMapper.insert(commit);
    }

    public CommitVO evaluate(Student2Resource student2Resource, Commit commit) throws Exception {

        QueryParam<Commit> paramsCommit = new QueryParam<>();
        paramsCommit.setCondition(commit);
        QueryParam<Student2Resource> paramsStudent2Resource = new QueryParam<>();
        paramsStudent2Resource.setCondition(student2Resource);
        List<Student2Resource> queryStudent2Resources = student2ResourceMapper.query(paramsStudent2Resource);
        // 找到 createTime 最大的 Commit 对象
        Map<String, Object> results = getResult(commit);
        Student2Resource queryStudent2Resource = queryStudent2Resources.getFirst();
        Map<String, Object> standards = getStandard(queryStudent2Resource);
        CommitVO commitVO = compare(results, standards);
        afterCompare(commitVO.getScore(), commit);
        return commitVO;
    }
}
