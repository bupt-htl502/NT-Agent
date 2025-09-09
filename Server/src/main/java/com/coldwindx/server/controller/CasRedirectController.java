package com.coldwindx.server.controller;

import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
public class CasRedirectController {

    // 从配置文件读取 CAS 核心参数（提前在 application.properties/yml 配置）
    @Value("${cas.server-login-url}")
    private String casServerLoginUrl;

    @Value("${cas.service-url}")
    private String casServiceUrl; // 后端 CAS 回调地址，即 http://10.101.170.78:5173/login/cas

    // 用户点击登录时，访问这个接口（如 http://10.101.170.78:5173/api/redirect-to-cas）
    @GetMapping("/api/redirect-to-cas")
    public void redirectToCas(HttpServletResponse response) throws IOException {
        // 1. 对 service 参数（后端回调地址）进行 URL 编码（CAS 协议要求）
        String encodedService = URLEncoder.encode(casServiceUrl, "UTF-8");
        // 2. 拼接完整的 CAS 登录地址
        String casLoginFullUrl = casServerLoginUrl + "?service=" + encodedService;
        // 3. 后端重定向到 CAS 登录页（浏览器会直接跳转到 CAS，无 CORS）
        response.sendRedirect(casLoginFullUrl);
    }
}