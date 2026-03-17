package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Class;
import com.coldwindx.server.service.ClassService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class ClassServiceImplTest {

    @Autowired
    private ClassService classService;

    @Test
    void testInsert() {
        // 创建测试班级
        Class clazz = new Class();
        clazz.setClassName("测试班级");
        clazz.setTeacherNo("20240001");
        clazz.setTeacherName("测试教师");

        // 插入班级
        int result = classService.insert(clazz);
        
        // 验证插入结果
        assertEquals(1, result); // 验证插入成功
        assertNotNull(clazz.getClassCode());
        assertEquals(6, clazz.getClassCode().length()); // 验证班级码是6位
    }

    @Test
    void testQuery() {
        // 创建测试班级
        Class clazz = new Class();
        clazz.setClassName("查询测试班级");
        clazz.setTeacherNo("20240002");
        clazz.setTeacherName("查询测试教师");
        classService.insert(clazz);

        // 构建查询参数
        QueryParam<Class> params = new QueryParam<>();
        Class condition = new Class();
        condition.setTeacherNo("20240002");
        params.setCondition(condition);

        // 查询班级
        List<Class> result = classService.query(params);
        
        // 验证查询结果
        assertNotNull(result);
        assertFalse(result.isEmpty());
        boolean found = result.stream().anyMatch(c -> c.getClassName().equals("查询测试班级"));
        assertTrue(found);
    }

    @Test
    void testQueryByCode() {
        // 创建测试班级
        Class clazz = new Class();
        clazz.setClassName("班级码测试班级");
        clazz.setTeacherNo("20240003");
        clazz.setTeacherName("班级码测试教师");
        classService.insert(clazz);

        // 通过班级码查询
        Class result = classService.queryByCode(clazz.getClassCode());
        
        // 验证查询结果
        assertNotNull(result);
        assertEquals(clazz.getClassName(), result.getClassName());
        assertEquals(clazz.getClassCode(), result.getClassCode());
    }

    @Test
    void testDelete() {
        // 创建测试班级
        Class clazz = new Class();
        String uniqueClassName = "删除测试班级" + System.currentTimeMillis(); // 使用时间戳确保班级名称唯一
        clazz.setClassName(uniqueClassName);
        clazz.setTeacherNo("20240004");
        clazz.setTeacherName("删除测试教师");
        classService.insert(clazz);

        // 查找刚创建的班级
        QueryParam<Class> params = new QueryParam<>();
        Class condition = new Class();
        condition.setClassName(uniqueClassName);
        params.setCondition(condition);
        List<Class> classes = classService.query(params);
        assertTrue(!classes.isEmpty());
        Class createdClass = classes.get(0);

        // 删除班级
        int result = classService.delete(createdClass);
        
        // 验证删除结果
        assertEquals(1, result);

        // 验证班级是否被删除
        List<Class> deletedClass = classService.query(params);
        assertTrue(deletedClass.isEmpty());
    }
}
