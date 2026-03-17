package com.coldwindx.server.controller;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Class;
import com.coldwindx.server.entity.form.RespResult;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.service.ClassService;
import com.coldwindx.server.service.StudentService;
import org.springframework.web.bind.annotation.*;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;

@RestController
@RequestMapping("/api/class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private StudentService studentService;

    /**
     * 创建班级
     * @param clazz 班级信息
     * @return 创建结果
     */
    @PostMapping("/create")
    public RespResult createClass(@RequestBody Class clazz) {
        int result = classService.insert(clazz);
        if (result > 0) {
            return new RespResult(200, "创建班级成功", clazz.getClassCode());
        } else {
            return new RespResult(500, "创建班级失败", null);
        }
    }

    /**
     * 查询班级列表
     * @param params 查询参数
     * @return 班级列表
     */
    @PostMapping("/query")
    public RespResult queryClass(@RequestBody QueryParam<Class> params) {
        List<Class> classes = classService.query(params);
        return new RespResult(200, "查询成功", classes);
    }

    /**
     * 通过班级码查询班级
     * @param classCode 班级码
     * @return 班级信息
     */
    @GetMapping("/queryByCode")
    public RespResult queryByCode(@RequestParam String classCode) {
        Class clazz = classService.queryByCode(classCode);
        if (clazz != null) {
            return new RespResult(200, "查询成功", clazz);
        } else {
            return new RespResult(404, "班级不存在", null);
        }
    }

    /**
     * 学生加入班级
     * @param studentNo 学生学号
     * @param classCode 班级码
     * @return 加入结果
     */
    @PostMapping("/join")
    public RespResult joinClass(@RequestParam String studentNo, @RequestParam String classCode) {
        // 1. 查找班级
        Class clazz = classService.queryByCode(classCode);
        if (clazz == null) {
            return new RespResult(404, "班级不存在", null);
        }

        // 2. 查找学生
        QueryParam<Student> studentQueryParam = new QueryParam<>();
        Student studentCondition = new Student();
        studentCondition.setStudentNo(studentNo);
        studentQueryParam.setCondition(studentCondition);
        List<Student> students = studentService.query(studentQueryParam);
        if (students.isEmpty()) {
            return new RespResult(404, "学生不存在", null);
        }

        Student student = students.get(0);
        // 3. 检查学生是否已经加入班级
        if (student.getClassId() != null) {
            return new RespResult(400, "学生已经加入班级，无法重复加入", null);
        }

        // 4. 更新学生的班级信息
        student.setClassId(clazz.getId());
        studentService.update(student);
        return new RespResult(200, "加入班级成功", null);
    }

    /**
     * 删除班级
     * @param id 班级ID
     * @return 删除结果
     */
    @PostMapping("/delete")
    public RespResult deleteClass(@RequestParam Long id) {
        Class clazz = new Class();
        clazz.setId(id);
        int result = classService.delete(clazz);
        if (result > 0) {
            return new RespResult(200, "删除班级成功", null);
        } else {
            return new RespResult(500, "删除班级失败", null);
        }
    }
}
