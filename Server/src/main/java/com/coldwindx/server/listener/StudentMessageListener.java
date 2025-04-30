package com.coldwindx.server.listener;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.manager.ApplicationContextRegister;
import com.coldwindx.server.service.EffectEvaluationService;
import com.coldwindx.server.service.SettingService;
import com.coldwindx.server.service.Student2ResourceService;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;


@Slf4j
@Component
public class StudentMessageListener {

    @Autowired
    private SettingService settingService;
    @Autowired
    private Student2ResourceService student2ResourceService;


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
    public void commition(Commit commit) throws Exception {
        log.info("queue {} received registration message: {}", "q_student_evaluation", commit);
        // 1. 根据 key=VUE_CONTENT_NODE 查询 t_setting，获取 id 与 commit.sceneId 一致的场景
        QueryParam<Setting> params = new QueryParam<>();
        params.getCondition().setKey("VUE_CONTENT_NODE");

        List<Setting> settingList = settingService.query(params);
        ObjectMapper objectMapper = new ObjectMapper();
        String sceneName = "";
        for (Setting setting : settingList) {
            String valueJson = setting.getValue();
            Integer id;
            try {
                JsonNode node = objectMapper.readTree(valueJson);
                id = node.path("id").asInt();
                System.out.println("Parsed ID: " + id);
                if (id.equals(commit.getSceneId())) {
                    sceneName = node.path("sceneName").asText();
                }
            } catch (Exception e) {
                log.error("Failed to parse setting value: {}", valueJson, e);
            }
        }
        // 2. 从实验场景中获取 效果评估服务 的抽象对象

        EffectEvaluationService service = ApplicationContextRegister.getBean(sceneName, EffectEvaluationService.class);

        // 3. 构造 效果评估服务 的参数，调用服务获取评估结果
        Student2Resource condition = new Student2Resource();
        condition.setStudentId(commit.getStudentId());
        condition.setSceneId(commit.getSceneId());
        QueryParam<Student2Resource> param = new QueryParam<>();
        param.setCondition(condition);
        List<Student2Resource> student2Resource = student2ResourceService.query(param);
        service.evaluate(student2Resource.getFirst());

    }
}