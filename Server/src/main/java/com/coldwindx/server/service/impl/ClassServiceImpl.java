package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Class;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.mapper.ClassMapper;
import com.coldwindx.server.service.ClassService;
import org.springframework.stereotype.Service;

import org.springframework.beans.factory.annotation.Autowired;
import java.util.List;
import java.util.Random;

@Service
public class ClassServiceImpl implements ClassService {

    @Autowired
    private ClassMapper classMapper;

    @Override
    public List<Class> query(QueryParam<Class> params,int role) {
        if(role == 2){
            return classMapper.queryAll();
        }
        return classMapper.query(params);
    }

    @Override
    public int insert(Class clazz,int tag) {
        //检查班级名称是否已存在
        if(tag == 0){
            QueryParam<Class> queryParamCheckName = new QueryParam<>();
            queryParamCheckName.setCondition(new Class());
            queryParamCheckName.getCondition().setClassName(clazz.getClassName());
            List<Class> existingNameClasses = classMapper.query(queryParamCheckName);
            if (!existingNameClasses.isEmpty()) {
                return -1;
            }
        }
        // 生成6位随机班级码
        String classCode = generateClassCode();
        // 检查班级码是否已存在
        QueryParam<Class> queryParamCheckCode = new QueryParam<>();
        queryParamCheckCode.setCondition(new Class());
        queryParamCheckCode.getCondition().setClassCode(classCode);
        List<Class> existingCodeClasses = classMapper.query(queryParamCheckCode);
        if (!existingCodeClasses.isEmpty()) {
            // 递归检查新的班级码是否已存在
            return insert(clazz,1);
        }
        clazz.setClassCode(classCode);
        return classMapper.insert(clazz);
    }

    @Override
    public int update(Class clazz) {
        return classMapper.update(clazz);
    }

    @Override
    public int delete(Class clazz) {
        return classMapper.delete(clazz);
    }

    @Override
    public Class queryByCode(String classCode) {
        return classMapper.queryByCode(classCode);
    }

    @Override
    public Class queryById(Long id) {
        return classMapper.queryById(id);
    }

    /**
     * 生成6位随机班级码
     * @return 6位随机班级码
     */
    private String generateClassCode() {
        String chars = "0123456789qwertyuiopasdfghjklzxcvbnm";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
