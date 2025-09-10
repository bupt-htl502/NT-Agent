package com.coldwindx.server.controller;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.net.URLEncoder;

@RestController
public class CasController {

    // 从配置文件读取 CAS 核心参数（提前在 application.properties/yml 配置）
    @Value("${cas.server-login-url}")
    private String casServerLoginUrl;

    @Value("${cas.server-logout-url}")
    private String casServerLogoutUrl;

    @Value("${cas.service-url}")
    private String casServiceUrl; // 后端 CAS 回调地址，即 http://10.101.170.78:5173/login/cas

    // 用户点击登录时，访问这个接口（如 http://10.101.170.78:5173/redirect-to-cas）
    @GetMapping("/login")
    public void casLogin(HttpServletResponse response) throws IOException {
        // 1. 对 service 参数（后端回调地址）进行 URL 编码（CAS 协议要求）
        String encodedService = URLEncoder.encode(casServiceUrl, "UTF-8");
        // 2. 拼接完整的 CAS 登录地址
        String casLoginFullUrl = casServerLoginUrl + "?service=" + encodedService;
        // 3. 后端重定向到 CAS 登录页（浏览器会直接跳转到 CAS，无 CORS）
        response.sendRedirect(casLoginFullUrl);
    }

    @GetMapping("/logout")
    public void logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // 1. 清理 SecurityContext
        SecurityContextHolder.clearContext();

        // 2. 清理本地 Session
        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        // 3. 清理自定义 Cookie
        String[] cookieNames = {"userName", "studentNo", "studentId"};
        for (String cookieName : cookieNames) {
            Cookie cookie = new Cookie(cookieName, null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            response.addCookie(cookie);
        }

        // 4. 拼接 CAS logout URL（带上 service 参数，登出后跳回你的前端首页）
        String encodedService = URLEncoder.encode(casServiceUrl, "UTF-8");
        String casLogoutFullUrl = casServerLogoutUrl + "?service=" + encodedService;

        // 5. 重定向到 CAS 登出
        response.sendRedirect(casLogoutFullUrl);
    }

}