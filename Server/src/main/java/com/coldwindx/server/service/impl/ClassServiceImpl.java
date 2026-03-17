package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Class;
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
    public List<Class> query(QueryParam<Class> params) {
        return classMapper.query(params);
    }

    @Override
    public int insert(Class clazz) {
        // 生成6位随机班级码
        String classCode = generateClassCode();
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

    /**
     * 生成6位随机班级码
     * @return 6位随机班级码
     */
    private String generateClassCode() {
        String chars = "0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < 6; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
}
