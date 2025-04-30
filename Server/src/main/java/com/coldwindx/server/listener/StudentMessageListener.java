package com.coldwindx.server.listener;

import com.alibaba.fastjson2.JSONObject;
import com.coldwindx.server.entity.form.Commit;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.entity.form.Setting;
import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student2Resource;
import com.coldwindx.server.manager.MinioMananger;
import com.coldwindx.server.service.SettingService;
import com.coldwindx.server.service.Student2ResourceService;
import io.minio.messages.Item;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class StudentMessageListener {
    @Autowired
    private SettingService settingService;

    @Autowired
    private Student2ResourceService student2ResourceService;

    @Autowired
    private MinioMananger minioMananger;

    @SneakyThrows
    @RabbitListener(queues = "q_student_register")
    public void registration(Student student) {
        // 1. 根据 key=VUE_CONTENT_NODE 查询 t_setting，构造全部 实验id 集合 [scenes]
        List<Integer> scenes = new ArrayList<>();

        QueryParam<Setting> params1 = new QueryParam<>();
        params1.condition = new Setting();
        params1.condition.setKey("VUE_CONTENT_NODE");

        List<Setting> list1 = settingService.query(params1);
        for(Setting setting : list1){
            String value = setting.getValue();
            if (value == null || value.trim().isEmpty()) {
                continue;
            }
            try {
                JSONObject obj = JSONObject.parseObject(value);
                Integer id = obj.getInteger("id");
                if (id != null) {
                    scenes.add(id);
                }
            } catch (Exception e) {
                log.error("解析JSON失败: value={}", value, e);
            }
        }

        // 2. 根据student_id查询t_student_to_resourse
        List<Integer> overlap = new ArrayList<>();

        QueryParam<Student2Resource> params2 = new QueryParam<>();
        params2.condition = new Student2Resource();
        params2.condition.setStudentId(student.getId());

        List<Student2Resource> list2 = student2ResourceService.query(params2);
        for(Student2Resource student2Resource : list2){
            Integer sceneID = student2Resource.getSceneId();
            if (sceneID == null) {
                continue;
            }
            overlap.add(sceneID);
        }

        // 3. 从scenes中排除在t_student_to_resourse中存在的实验场景
        List<Integer> difference = new ArrayList<>(scenes);
        difference.removeAll(overlap);

        // 4. 对剩余的scenes，查询minio服务，填充数据&答案路径 [目前可以随机生成]
        List<Student2Resource> s2r = new ArrayList<>();
        for (Integer scene : difference) {
            List<Item> resultDatasets = minioMananger.getAllObjectsByPrefix("1111", String.format("/home/path/%d", scene), false);
            List<Item> resultAnswers = minioMananger.getAllObjectsByPrefix("1111", String.format("/home/criterion/%d", scene), false);

            String datasetpath = null;
            String answerpath = null;
            if (!resultDatasets.isEmpty() && !resultAnswers.isEmpty()) {
                Collections.shuffle(resultDatasets);
                Collections.shuffle(resultAnswers);

                Item dataset = resultDatasets.getFirst();
                Item answer = resultAnswers.getFirst();

                datasetpath = "/home/path" + dataset.objectName();
                answerpath = "/home/criterion" + answer.objectName();
            }

            // 5. 剩余场景构造新的t_student_to_resourse表项
            Student2Resource student2Resource = new Student2Resource();
            student2Resource.setStudentId(student.getId());
            student2Resource.setSceneId(scene);
            student2Resource.setPath(datasetpath);
            student2Resource.setCriterion(answerpath);
            student2Resource.setCreateTime(System.currentTimeMillis());

            s2r.add(student2Resource);
        }
        // 6. 批量插入t_student_to_resourse
        student2ResourceService.insertList(s2r);
    }

    @SneakyThrows
    @RabbitListener(queues = "q_student_evaluation")
    public void commition(Commit commit){
        log.info("queue {} received registration message: {}", "q_student_evaluation", commit);
        // 1. 根据 key=VUE_CONTENT_NODE 查询 t_setting，获取 id 与 commit.sceneId 一致的场景
        // 2. 从实验场景中获取 效果评估服务 的抽象对象
        // 3. 构造 效果评估服务 的参数，调用服务获取评估结果
        // 4. 结果更新至t_commit
    }
}
