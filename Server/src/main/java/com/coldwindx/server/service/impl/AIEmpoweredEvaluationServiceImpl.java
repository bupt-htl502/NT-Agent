package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.CommitVO;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.service.EffectEvaluationService;
import com.coldwindx.server.utils.MinioUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@Service("aiEmpoweredEvaluationServiceImpl")
public class AIEmpoweredEvaluationServiceImpl extends EffectEvaluationService {

    @Autowired
    private MinioUtils minioUtils;
    @Autowired
    private StringFeaturesEvaluationServiceImpl stringFeaturesEvaluationServiceImpl;

    @Override
    public CommitVO compare(Map<String, Object> results, Map<String, Object> standards){
        CommitVO commitVO = new CommitVO();
        double TP = 0.0;
        double FP = 0.0;
        double FN = 0.0;
        double TN = 0.0;
        for(String key:standards.keySet()){
            String answer = extractSingleValue(standards.get(key));
            String result = extractSingleValue(results.getOrDefault(key, -1));
            System.out.println(answer + " " + result);
            if(answer.equals("0")&&result.equals("0")){
                TN++;
            }else if(answer.equals("0")){
                FN++;
            }else if(answer.equals("1")&&result.equals("1")){
                TP++;
            }else{
                FP++;
            }
        }
        double Precision = TP/(TP+FP);
        double Recall = TP/(TP+FN);
        double F1 = 2*(Precision*Recall)/(Precision+Recall);

        commitVO.setScore(f1ToScore(F1));
        if(commitVO.getScore()<60){
            commitVO.setRemark("F1 Score为"+F1+"\n不及格");
        }else{
            commitVO.setRemark("F1 Score为"+F1);
        }

        return commitVO;
    }
    @Override
    protected Map<String, Object> getStandard(Student2Resource student2Resource) throws Exception {
        return minioUtils.loadStringFromCSV(student2Resource.getCriterion());
    }

    @Override
    protected Map<String, Object> getResult(Commit commit) throws Exception {
        return  minioUtils.loadStringFromCSV(commit.getPath());
    }

    public static Double f1ToScore(double f1) {
        if (f1 < 0.7) {
            // 比例线性得分（低于60分）
            return (double) Math.max(0, (int)Math.round((f1 / 0.7) * 60));
        } else if (f1 <= 0.9) {
            // 在线性区间 [0.7, 0.9] 映射到 [60, 100]
            return (double) Math.round(60 + (f1 - 0.7) * 200);
        } else {
            // 上限封顶100分
            return 100.0;
        }
    }
    private String extractSingleValue(Object obj) {
        if (obj instanceof Object[] && ((Object[]) obj).length > 0) {
            return ((Object[]) obj)[0].toString();
        }
        return obj.toString();
    }
}
