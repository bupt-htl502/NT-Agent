package com.coldwindx.server.service.impl;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.service.StudentService;
import jakarta.annotation.Resource;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class CustomCasUserDetailsService
        implements AuthenticationUserDetailsService<CasAssertionAuthenticationToken> {

    @Resource
    private StudentService studentService;

    @Override
    public UserDetails loadUserDetails(CasAssertionAuthenticationToken token) throws UsernameNotFoundException {
        // 获取 CAS principal
        String username = token.getName();

        // 获取 CAS 返回的属性
        Map<String, Object> attributes = token.getAssertion().getPrincipal().getAttributes();
        String name = (String) attributes.get("name");
        String employeeNumber = (String) attributes.get("employeenumber");

        // 可以把这些信息存数据库，或者存到 SecurityContext
        System.out.println("用户名：" + username + ", 姓名：" + name + ", 工号：" + employeeNumber);

        int role = studentService.queryAndInsert(name,employeeNumber);
        if(role==200){
            return User.withUsername(username)
                    .password("") // CAS 已认证
                    .roles("TEACHER")
                    .build();
        }
        return User.withUsername(username)
                .password("") // CAS 已认证
                .roles("STUDENT")
                .build();
    }
}

