package com.coldwindx.server.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
public class CookieAuthFilter extends OncePerRequestFilter {

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        // 先检查 session 是否已有 SecurityContext
        HttpSession session = request.getSession(false);
        if (session != null) {
            Object contextObj = session.getAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY);
            if (contextObj instanceof SecurityContext sc && sc.getAuthentication() != null) {
                System.out.println("CookieAuthFilter: SecurityContext already exists in session, skipping cookie check");
                filterChain.doFilter(request, response);
                return;
            }
        }

        // 从 Cookie 获取身份信息
        Cookie[] cookies = request.getCookies();
        String studentName = null;
        String studentNo = null;
        String studentId = null;

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                switch (cookie.getName()) {
                    case "studentName" -> studentName = cookie.getValue();
                    case "studentNo" -> studentNo = cookie.getValue();
                    case "studentId" -> studentId = cookie.getValue();
                }
            }
        }

        if (studentName != null && studentNo != null && studentId != null) {
            System.out.printf("CookieAuthFilter: Cookies found: studentName=%s, studentNo=%s, studentId=%s%n",
                    studentName, studentNo, studentId);

            // 构建一个简单的 Authentication 对象，roles 为空
            UsernamePasswordAuthenticationToken auth =
                    new UsernamePasswordAuthenticationToken(
                            new SimpleUser(studentName, studentNo, Long.valueOf(studentId)),
                            null,
                            List.of()
                    );
            SecurityContextHolder.getContext().setAuthentication(auth);

            // 写入 session
            if (session == null) {
                session = request.getSession(true);
            }
            session.setAttribute(
                    HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY,
                    SecurityContextHolder.getContext()
            );

            System.out.println("CookieAuthFilter: SecurityContext set and written to session");
        } else {
            System.out.println("CookieAuthFilter: Required cookies not present, skipping authentication");
        }

        filterChain.doFilter(request, response);
    }

    // 简单封装用户信息，存入 Authentication principal
    public static class SimpleUser {
        private final String studentName;
        private final String studentNo;
        private final Long studentId;

        public SimpleUser(String studentName, String studentNo, Long studentId) {
            this.studentName = studentName;
            this.studentNo = studentNo;
            this.studentId = studentId;
        }

        public String getUserName() { return studentName; }
        public String getStudentNo() { return studentNo; }
        public Long getStudentId() { return studentId; }
    }
}
