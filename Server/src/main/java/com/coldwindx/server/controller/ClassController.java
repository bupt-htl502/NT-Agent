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
import java.util.Map;
import java.util.HashMap;

@RestController
@RequestMapping("class")
public class ClassController {

    @Autowired
    private ClassService classService;

    @Autowired
    private StudentService studentService;

    /**
     * 验证用户是否为教师角色（role=200）
     * @param studentNo 学生学号
     * @return 是否为教师
     */
    private int isTeacherOrAdmin(String studentNo) {
        QueryParam<Student> studentQueryParam = new QueryParam<>();
        Student studentCondition = new Student();
        studentCondition.setStudentNo(studentNo);
        studentQueryParam.setCondition(studentCondition);
        List<Student> students = studentService.query(studentQueryParam);
        if (students.isEmpty()) {
            return -1;
        }
        Student student = students.get(0);
        if (student.getRole() == 200){
            return 1;
        }else if (student.getRole() == 300){
            return 2;
        }else{
            return 0;
        }
        
    }

    /**
     * 创建班级
     * @param className 班级名称
     * @param studentNo 学生学号
     * @param studentName 学生姓名
     * @return 创建结果
     */
    @PostMapping("/create")
    public RespResult createClass(@RequestBody Map<String, Object> requestBody) {
        // 从请求体中获取参数
        String className = (String) requestBody.get("className");
        String studentNo = (String) requestBody.get("studentNo");
        String studentName = (String) requestBody.get("studentName");
        int role = isTeacherOrAdmin(studentNo);
        
        // 验证用户是否为教师角色
        if (role==-1||role==0) {
            return new RespResult(403, "权限不足，只有教师才能创建班级", null);
        }
        
        // 创建班级
        Class clazz = new Class();
        clazz.setClassName(className);
        clazz.setTeacherNo(studentNo);
        clazz.setTeacherName(studentName);
        int result = classService.insert(clazz,0);
        Map<String,String> resp = new HashMap<>();
        if (result > 0) {
            resp.put("classCode",clazz.getClassCode());
            resp.put("message","创建班级成功");
            return new RespResult(0, "创建班级成功", resp);
        } else if(result == -1){
            return new RespResult(1003, "班级名称已存在", null);
        } else {
            return new RespResult(-1, "创建班级失败", null);
        }
    }

    /**
     * 查询老师的班级列表
     * @param params 查询参数
     * @return 班级列表
     */
    @PostMapping("/query")
    public RespResult queryClass(@RequestBody Map<String, Object> requestBody) {
        // 从请求体中获取参数
        String teacherNo = (String) requestBody.get("teacherNo");
        int role = isTeacherOrAdmin(teacherNo);
        if(role==0||role==-1){
             return new RespResult(1003, "权限不足，只有教师才能查看班级", null);
        }
        // 查询班级列表
        QueryParam<Class> params = new QueryParam<>();
        Class condition = new Class();
        condition.setTeacherNo(teacherNo);
        params.setCondition(condition);
        List<Class> classes = classService.query(params,role);
        
        Map<String,String> resp = new HashMap<>();
        resp.put("message","查询成功");
        resp.put("classes",classes.toString());
        return new RespResult(0, "查询成功", resp);
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
            Map<String,String> resp = new HashMap<>();
            resp.put("message","查询成功");
            resp.put("className",clazz.getClassName());
            return new RespResult(0, "查询成功", resp);
        } else {
            return new RespResult(1003, "班级不存在", null);
        }
    }

    /**
     * 学生加入班级
     * @param studentNo 学生学号
     * @param classCode 班级码
     * @return 加入结果
     */
    @PostMapping("/join")
    public RespResult joinClass(@RequestBody Map<String, Object> requestBody) {
        // 1. 查找班级
        String classCode = (String) requestBody.get("classCode");
        Class clazz = classService.queryByCode(classCode);
        if (clazz == null) {
            return new RespResult(1003, "班级不存在", null);
        }

        // 2. 查找学生
        String studentNo = (String) requestBody.get("studentNo");
        QueryParam<Student> studentQueryParam = new QueryParam<>();
        Student studentCondition = new Student();
        studentCondition.setStudentNo(studentNo);
        studentQueryParam.setCondition(studentCondition);
        List<Student> students = studentService.query(studentQueryParam);
        if (students.isEmpty()) {
            return new RespResult(1003, "学生不存在", null);
        }

        Student student = students.get(0);
        // 3. 检查学生是否已经加入班级
        if (student.getClassId() != null&&student.getClassId()!=-1L) {
            return new RespResult(1003, "学生已经加入班级，无法重复加入", null);
        }

        // 4. 更新学生的班级信息
        student.setClassId(clazz.getId());
        student.setClassName(clazz.getClassName());
        studentService.update(student);
        Map<String,String> resp = new HashMap<>();
        resp.put("message","加入班级成功");
        resp.put("className",clazz.getClassName());
        return new RespResult(0, "加入班级成功", resp);
    }
    /**
     * 退出班级
     * @param studentNo
     * @return
     */
    @PostMapping("/quit")
    public RespResult quitClass(@RequestBody Map<String,Object> requestBody){
        String studentNo = (String) requestBody.get("studentNo");
        QueryParam<Student> studentQueryParam = new QueryParam<>();
        Student studentCondition = new Student();
        studentCondition.setStudentNo(studentNo);
        studentQueryParam.setCondition(studentCondition);
        List<Student> students = studentService.query(studentQueryParam);
        if (students.isEmpty()) {
            return new RespResult(1003, "学生不存在", null);
        }
        Student student = students.get(0);
        student.setClassId(-1L);
        student.setClassName("noClass");
        studentService.update(student);
        Map<String,String> resp = new HashMap<>();
        resp.put("message","退出班级成功");
        return new RespResult(0, "退出班级成功", resp);
    }
    /**
     * 删除班级
     * @param id 班级ID
     * @param studentNo 学生学号
     * @return 删除结果
     */
    @PostMapping("/delete")
    public RespResult deleteClass(@RequestBody Map<String, Object> requestBody) {
        // 从请求体中获取参数
        Long id = (Long) requestBody.get("id");
        String studentNo = (String) requestBody.get("studentNo");
        int role = isTeacherOrAdmin(studentNo);
        // 验证用户是否为教师角色
        if (role==-1||role==0) {
            return new RespResult(1003, "权限不足，只有教师才能删除班级", null);
        }
        
        // 查询班级信息，验证是否为班级拥有者
        Class existingClass = classService.queryById(id);
        if (existingClass == null) {
            return new RespResult(1003, "班级不存在", null);
        }
        
        // 验证是否为班级拥有者
        if (!studentNo.equals(existingClass.getTeacherNo())) {
            return new RespResult(1003, "权限不足，您不是该班级的拥有者", null);
        }
        
        Class clazz = new Class();
        clazz.setId(id);
        int result = classService.delete(clazz);
        Map<String,String> resp = new HashMap<>();
        if (result > 0) {
            resp.put("message","删除班级成功");
            return new RespResult(0, "删除班级成功", resp);
        } else {
            return new RespResult(-1, "删除班级失败", null);
        }
    }

}
