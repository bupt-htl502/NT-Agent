package com.coldwindx.server.listener;

import com.coldwindx.server.entity.NtAgentException;
import com.coldwindx.server.entity.QueryParam;
import com.alibaba.fastjson2.JSONObject;
import com.coldwindx.server.entity.StudentRegistrationMessage;
import com.coldwindx.server.entity.enums.ResponseCode;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.manager.ApplicationContextRegister;
import com.coldwindx.server.service.EffectEvaluationService;
import com.coldwindx.server.service.SettingService;
import com.coldwindx.server.service.Student2ResourceService;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.impl.NumericalCharacteristicsEvaluationServiceImpl;
import io.minio.messages.Item;
import jakarta.annotation.Resource;
import kotlin.Pair;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;


import java.util.Collections;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
@Component
public class StudentMessageListener {
    @Autowired
    private SettingService settingService;

    @Autowired
    private Student2ResourceService student2ResourceService;

    @Autowired
    private MinioMananger minioMananger;

    @Resource(name = "numericalCharacteristicsEvaluationServiceImpl")
    private EffectEvaluationService service;

    @SneakyThrows
    @RabbitListener(queues = "q_student_register_test")
    public void registration(StudentRegistrationMessage studentRegistrationMessage) {
        Student student = studentRegistrationMessage.student;
        boolean isTestMode = studentRegistrationMessage.isTestMode;
        // 1. 根据 key=VUE_CONTENT_NODE 查询 t_setting，构造全部 实验id 集合 [scenes]
        QueryParam<Setting> params1 = new QueryParam<>();
        params1.condition = new Setting();
        params1.condition.setKey("VUE_CONTENT_NODE");
        List<Setting> list1 = settingService.query(params1);
        Stream<Integer> stream = list1.stream().map(Setting::getValue).map(JSONObject::parseObject).map(obj -> obj.getInteger("id"));

        // 2. 根据student_id查询t_student_to_resourse
        QueryParam<Student2Resource> params2 = new QueryParam<>();
        params2.condition = new Student2Resource();
        params2.condition.setStudentId(student.getId());
        List<Student2Resource> list2 = student2ResourceService.query(params2);
        List<Integer> excludes = list2.stream().map(Student2Resource::getSceneId).toList();

        // 3. 从scenes中排除在t_student_to_resourse中存在的实验场景
        stream = stream.filter(id -> !excludes.contains(id));

        // 4. 对剩余的scenes，查询minio服务，填充数据&答案路径 [目前可以随机生成]
//        Integer id = 40003;
//        Student2Resource student2Resource = new Student2Resource();
        log.warn("student id {}", student.getId());
        List<Student2Resource> s2r = stream.map(id -> {
            try {
                List<Item> resultDatasets = minioMananger.getAllObjectsByPrefix("datasets", String.format("experiment/%d/", id), false);
                List<Item> resultAnswers = minioMananger.getAllObjectsByPrefix("datasets", String.format("answer/%d/", id), false);

                if (resultDatasets.isEmpty() || resultAnswers.isEmpty()) {
//                    log.warn("❌ id={} 对应的数据或答案为空，跳过", id);
                    return null; // 标记为无效
//                    throw new NtAgentException(ResponseCode.RESOURCES_NOT_EXIST.getCode(), "数据&答案不存在，请检查服务器信息！");
                }
                List<Pair<Item, Item>> pairedList = new ArrayList<>();
                for (int i = 0; i < Math.min(resultDatasets.size(), resultAnswers.size()); i++) {
                    pairedList.add(new Pair<>(resultDatasets.get(i), resultAnswers.get(i)));
                }
                if(!isTestMode){
                    Collections.shuffle(pairedList);
                }

                Item dataset = pairedList.getFirst().component1();
                Item answer = pairedList.getFirst().component2();

                // 5. 剩余场景构造新的t_student_to_resourse表项
                Student2Resource student2Resource = new Student2Resource();
                student2Resource.setStudentId(student.getId());
                student2Resource.setSceneId(id);
                student2Resource.setPath("datasets/" + dataset.objectName());
                student2Resource.setCriterion("datasets/" + answer.objectName());
                student2Resource.setCreateTime(System.currentTimeMillis());
//                log.warn("√ 处理 scene id={} 完成", id);
                return student2Resource;
            } catch (Exception e) {
//                throw new RuntimeException(e);
//                log.warn("❌ 处理 scene id={} 时发生异常，跳过该 id：{}", id, e.getMessage());
                return null; // 出异常则跳过
            }
        }).filter(Objects::nonNull).toList();
        if(s2r.isEmpty()){
            return;
        }
        // 6. 批量插入t_student_to_resource
        student2ResourceService.batchInsert(s2r);
//        student2ResourceService.insert(student2Resource);
    }

    @SneakyThrows
    @RabbitListener(queues = "q_student_evaluation")
    public void commition(Commit commit) throws Exception {
        log.info("queue {} received registration message: {}", "q_student_evaluation", commit);
        // 1. 根据 key=VUE_CONTENT_NODE 查询 t_setting，获取 id 与 commit.sceneId 一致的场景
        QueryParam<Setting> params = new QueryParam<>();
        params.getCondition().setKey("VUE_CONTENT_NODE");
        List<Setting> settings = settingService.query(params);
        String[] services = settings.stream().map(Setting::getValue)
                .map(JSONObject::parseObject)
                .filter(obj-> commit.getSceneId().equals(obj.getInteger("id")))
                .map(obj -> obj.getString("service"))
                .toArray(String[]::new);
        if(0 == services.length)
            throw new NtAgentException(ResponseCode.SERVICE_ERROR.getCode(), "实验场景不存在！");
        if(1 < services.length)
            throw new NtAgentException(ResponseCode.SERVICE_ERROR.getCode(), "实验场景重复配置！");

        // 2. 从实验场景中获取 效果评估服务 的抽象对象
        // EffectEvaluationService service = ApplicationContextRegister.getBean(services[0], EffectEvaluationService.class);

        // 3. 构造 效果评估服务 的参数，调用服务获取评估结果
        Student2Resource student2Resource = new Student2Resource();
        student2Resource.setStudentId(commit.getStudentId());
        student2Resource.setSceneId(commit.getSceneId());
        service.evaluate(student2Resource, commit);
//        Student2Resource condition = new Student2Resource();
//        condition.setStudentId(commit.getStudentId());
//        condition.setSceneId(commit.getSceneId());
//        QueryParam<Student2Resource> param = new QueryParam<>();
//        param.setCondition(condition);
//        List<Student2Resource> student2Resource = student2ResourceService.query(param);
//        service.evaluate(student2Resource.getFirst(), commit);

    }

}
