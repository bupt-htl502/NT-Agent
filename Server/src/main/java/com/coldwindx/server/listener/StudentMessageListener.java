package com.coldwindx.server.listener;

import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.manager.ApplicationContextRegister;
import com.coldwindx.server.service.EffectEvaluationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class StudentMessageListener {

    @RabbitListener(queues = "q_student_register")
    public void registration(Student student) {
        // 1. 根据 key=VUE_CONTENT_NODE 查询 t_setting，构造全部 实验id 集合 [scenes]
        // 2. 根据student_id查询t_student_to_resourse
        // 3. 从scenes中排除在t_student_to_resourse中存在的实验场景
        // 4. 对剩余的scenes，查询minio服务，填充数据&答案路径 [目前可以随机生成]
        // 5. 剩余场景构造新的t_student_to_resourse表项
        // 6. 批量插入t_student_to_resourse
    }

    @RabbitListener(queues = "q_student_evaluation")
    public void commition(Commit commit){
        log.info("queue {} received registration message: {}", "q_student_evaluation", commit);
        // 1. 根据 key=VUE_CONTENT_NODE 查询 t_setting，获取 id 与 commit.sceneId 一致的场景
//        service
        EffectEvaluationService service = ApplicationContextRegister.getBean("service", EffectEvaluationService.class);
        // 2. 从实验场景中获取 效果评估服务 的抽象对象
        // 3. 构造 效果评估服务 的参数，调用服务获取评估结果
        // 4. 结果更新至t_commit
    }
}
