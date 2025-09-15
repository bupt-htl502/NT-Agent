package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.SceneScoreVo;
import com.coldwindx.server.entity.StudentScoreVo;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.SceneInfo;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.mapper.CommitMapper;
import com.coldwindx.server.mapper.StudentMapper;
import com.coldwindx.server.service.CommitService;
import com.coldwindx.server.service.EffectEvaluationService;
import com.coldwindx.server.service.SettingService;
import jakarta.annotation.Resource;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class CommitServiceImpl implements CommitService {
    @Autowired
    private CommitMapper commitMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Resource(name = "multiCSVStringFeaturesEvaluationServiceImpl")
    private MultiCSVStringFeaturesEvaluationServiceImpl multiCSVStringService;
    @Resource(name = "numericalCharacteristicsEvaluationServiceImpl")
    private EffectEvaluationService numericalService;

    @Resource(name = "stringFeaturesEvaluationServiceImpl")
    private EffectEvaluationService stringService;

    @Resource(name = "pcapCleaningFilteringSplittingEvaluationServiceImpl")
    private EffectEvaluationService pcapCleaningFilteringSplittingService;

    @Resource(name = "pcapSortingEvaluationServiceImpl")
    private EffectEvaluationService pcapSortingService;

    @Resource(name = "aiEmpoweredEvaluationServiceImpl")
    private EffectEvaluationService aiService;

    @Resource(name = "pcapFilteringEvaluationServiceImpl")
    private EffectEvaluationService pcapFilteringService;

    @Resource
    private SettingService settingService;

    @Autowired
    private StudentMapper studentMapper;

    @Override
    public List<Commit> query(QueryParam<Commit> params) {
        return commitMapper.query(params);
    }

    @Override
    public CommitVO insert(Commit commit) throws Exception {
        // 通知效果评估服务，执行评估
        // rabbitTemplate.convertAndSend("ex_student", "commited", commit);
        //    @Resource(name = "numericalCharacteristicsEvaluationServiceImpl")
        EffectEvaluationService service;
        int sceneid = commit.getSceneId();
        if(sceneid==10002||sceneid==10007||sceneid==10009){
            Student student = new Student();
            student.setId(commit.getStudentId());
            student.setNowScene(sceneid);
            studentMapper.update(student);
            CommitVO commitVO = new CommitVO();
            commitVO.setScore(100.0);
            commitVO.setRemark("作业有效！");
            commit.setScore(100.0);
            commitMapper.insert(commit);
            return commitVO;
        }
        else if(sceneid==10004||sceneid==10005) {
            service = pcapFilteringService;
        }
        else if(sceneid == 20002 || sceneid == 20005 || sceneid == 20006) {
            service = pcapCleaningFilteringSplittingService;
        }
        else if(sceneid == 20003) {
            service = pcapSortingService;
        }
        else if(sceneid == 40003 || sceneid == 40004 || sceneid == 40011) {
            service = numericalService;
        }
        else if(sceneid == 40002 || sceneid == 40007){
            service = multiCSVStringService;
        }else if(sceneid>50000){
            service = aiService;
        }
        else {
            service = stringService;
        }
        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(commit.getStudentId());
        student2Resource.setSceneId(sceneid);
        return service.evaluate(student2Resource, commit);
    }

    @Override
    public List<StudentScoreVo> getScoreList(List<Student> students) {
        List<StudentScoreVo> studentScoreList = new ArrayList<>();

        for (Student student : students) {
            QueryParam<Commit> queryParam = new QueryParam<>();
            Commit condition = new Commit();
            condition.setStudentId(student.getId());
            condition.setIsdeleted(false);
            queryParam.setCondition(condition);
            List<Commit> commitList = query(queryParam);

            StudentScoreVo studentScore = new StudentScoreVo();
            studentScore.setStudentNo(student.getStudentNo());
            studentScore.setName(student.getName());

            List<StudentScoreVo.ScorePerScene> scorePerScenes = new ArrayList<>();
            Double averageScore = 0.0;
            int commitTimes = 0;

            for (Commit commit : commitList) {
                StudentScoreVo.ScorePerScene scorePerScene = new StudentScoreVo.ScorePerScene();
                Integer sceneId = commit.getSceneId();
                SceneInfo sceneInfo = settingService.getSceneInfo(sceneId);
                scorePerScene.setChapterName(sceneInfo.getChapterName());
                scorePerScene.setTaskName(sceneInfo.getTaskName());
                scorePerScene.setSceneName(sceneInfo.getSceneName());
                scorePerScene.setScore(commit.getScore());
                scorePerScene.setCommitTime(0);

                averageScore += commit.getScore();
                commitTimes += 0;
                scorePerScenes.add(scorePerScene);
            }

            averageScore = averageScore / commitList.size();
            studentScore.setAverageScore(averageScore);
            studentScore.setCommitTimes(commitTimes);
            studentScore.setScores(scorePerScenes);
            studentScoreList.add(studentScore);
        }

        return studentScoreList;
    }

    @Override
    public List<SceneScoreVo> getSceneAverage() {
        List<SceneInfo> sceneInfoList = settingService.getSceneInfoList();
        List<SceneScoreVo> sceneScoreList = new ArrayList<>();

        for (SceneInfo sceneInfo : sceneInfoList) {
            QueryParam<Commit> queryParam = new QueryParam<>();
            Commit condition = new Commit();
            condition.setIsdeleted(false);
            condition.setSceneId(sceneInfo.getSceneId());
            queryParam.setCondition(condition);
            List<Commit> commitList = query(queryParam);

            Double scoreSum = 0.0;
            int commitTimes = 0;
            for (Commit commit : commitList) {
                scoreSum += commit.getScore();
                commitTimes += 0;
            }
            Double averageScore = scoreSum / commitList.size();
            Double averageCommitTimes = (double) commitTimes / commitList.size();

            SceneScoreVo sceneScore = new SceneScoreVo();
            sceneScore.setChapterName(sceneInfo.getChapterName());
            sceneScore.setTaskName(sceneInfo.getTaskName());
            sceneScore.setSceneName(sceneInfo.getSceneName());
            sceneScore.setAverageScore(averageScore);
            sceneScore.setAverageCommitTimes(averageCommitTimes);
            sceneScoreList.add(sceneScore);
        }

        return sceneScoreList;
    }
}
