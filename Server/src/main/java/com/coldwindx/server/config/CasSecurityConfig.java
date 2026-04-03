package com.coldwindx.server.config;

import com.coldwindx.server.entity.QueryParam;
import com.coldwindx.server.entity.form.Student;
import com.coldwindx.server.service.StudentService;
import com.coldwindx.server.service.impl.CustomCasUserDetailsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import org.apereo.cas.client.validation.Cas30ProxyTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.authentication.CasAuthenticationToken;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

import com.coldwindx.server.filter.CookieAuthFilter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class CasSecurityConfig {

    @Resource
    private CustomCasUserDetailsService customCasUserDetailsService;

    @Resource
    private StudentService studentService;

    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;

    @Value("${cas.server-login-url}")
    private String casServerLoginUrl;

    @Value("${cas.service-url}")
    private String casServiceUrl;

    @Value("${cas.server-logout-url}")
    private String casServerLogoutUrl;

    // 配置服务属性
    @Bean
    public ServiceProperties serviceProperties() {
        ServiceProperties serviceProperties = new ServiceProperties();
        serviceProperties.setService(casServiceUrl);
        serviceProperties.setSendRenew(false);
        return serviceProperties;
    }

    // CAS认证入口点
    @Bean
    public CasAuthenticationEntryPoint casAuthenticationEntryPoint(ServiceProperties serviceProperties) {
        CasAuthenticationEntryPoint entryPoint = new CasAuthenticationEntryPoint();
        entryPoint.setLoginUrl(casServerLoginUrl);
        entryPoint.setServiceProperties(serviceProperties);
        return entryPoint;
    }

    // CAS认证过滤器
    @Bean
    public CasAuthenticationFilter casAuthenticationFilter(AuthenticationManager authenticationManager,
                                                           ServiceProperties serviceProperties,
                                                           AuthenticationSuccessHandler successHandler) {
        CasAuthenticationFilter filter = new CasAuthenticationFilter();
        filter.setAuthenticationManager(authenticationManager);
        filter.setServiceProperties(serviceProperties);
        filter.setAuthenticationSuccessHandler(successHandler);
        // 设置CAS过滤器只拦截/login/cas路径
        filter.setFilterProcessesUrl("/login/cas");
        return filter;
    }

    // CAS认证提供者
    @Bean
    public CasAuthenticationProvider casAuthenticationProvider(ServiceProperties serviceProperties) {
        CasAuthenticationProvider provider = new CasAuthenticationProvider();
        provider.setServiceProperties(serviceProperties);
        provider.setTicketValidator(new Cas30ProxyTicketValidator(casServerUrlPrefix));
        provider.setKey("casAuthenticationProviderKey");

        // 设置用户详情服务
        provider.setAuthenticationUserDetailsService(customCasUserDetailsService);

        return provider;
    }

    // 认证管理器
    @Bean
    public AuthenticationManager authenticationManager(CasAuthenticationProvider casAuthenticationProvider) {
        return new ProviderManager(casAuthenticationProvider);
    }

    // 1. 合并登出逻辑：使用Spring Security的logout配置替代独立接口和过滤器
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CasAuthenticationEntryPoint entryPoint,
                                                   CasAuthenticationFilter casFilter,
                                                   CookieAuthFilter cookieAuthFilter) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .securityContext(securityContext -> securityContext
                        .requireExplicitSave(false)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/redirect-to-cas", "/login/cas", "/home", "/logout/callback", "/login").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                        .invalidSessionUrl(casServerLoginUrl + "?service=" + casServiceUrl)
                        .sessionFixation().migrateSession()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                )
                // 核心：配置统一的登出流程
                .logout(logout -> logout
                        .logoutUrl("/logout") // 唯一登出入口，前端调用此地址
                        .addLogoutHandler(customLogoutHandler()) // 执行本地清理（替代原/logout接口逻辑）
                        .logoutSuccessHandler(casLogoutSuccessHandler()) // 登出成功后处理（替代LogoutFilter）
                        .invalidateHttpSession(true) // 自动销毁Session（与session.invalidate()等效）
                        .clearAuthentication(true) // 清除认证信息（与SecurityContextHolder.clearContext()等效）
                        .permitAll() // 允许匿名访问登出入口
                )
                .addFilterBefore(cookieAuthFilter, CasAuthenticationFilter.class)
                .addFilterBefore(casFilter, LogoutFilter.class);

        return http.build();
    }

    // 2. 自定义登出处理器：包含原/logout接口中的清理逻辑
    @Bean
    public LogoutHandler customLogoutHandler() {
        return (request, response, authentication) -> {
            System.out.println("执行本地登出清理");

            // 清理自定义Cookie（原步骤3）
            String[] cookieNames = {"studentName", "studentNo", "studentId", "JSESSIONID"};
            for (String cookieName : cookieNames) {
                Cookie cookie = new Cookie(cookieName, null);
                cookie.setMaxAge(0);
                cookie.setPath("/");
                response.addCookie(cookie);
            }

        };
    }

    // 3. 登出成功处理器：替代原LogoutFilter的重定向逻辑
    @Bean
    public LogoutSuccessHandler casLogoutSuccessHandler() {
        return (request, response, authentication) -> {
            // 直接重定向到前端登录页面
            response.sendRedirect("http://10.101.162.248:5174/login");
        };
    }

//登录成功配置器
    @Bean
    public AuthenticationSuccessHandler casAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 获取用户名、扩展属性
            CasAuthenticationToken token = (CasAuthenticationToken) authentication;
            String employeeNumber = token.getName();
            Map<String, Object> attributes = token.getAssertion().getPrincipal().getAttributes();
            String name = (String) attributes.get("name");

            QueryParam<Student> params = new QueryParam<>();
            params.setCondition(new Student());
            params.getCondition().setName(name);
            params.getCondition().setStudentNo(employeeNumber);

            List<Student> student = studentService.query(params);

            // 可以设置到 Cookie
            Cookie nameCookie = new Cookie("studentName", URLEncoder.encode(name, "UTF-8"));
            nameCookie.setPath("/");
            nameCookie.setHttpOnly(false); // 前端JS可访问
            nameCookie.setMaxAge(60 * 60 * 24 * 8); // 8天

            Cookie empCookie = new Cookie("studentNo", employeeNumber);
            empCookie.setPath("/");
            empCookie.setHttpOnly(false);
            empCookie.setMaxAge(60 * 60 * 24 * 8);

            Cookie idCookie = new Cookie("studentId", String.valueOf(student.getFirst().getId()));
            idCookie.setPath("/");
            idCookie.setHttpOnly(false);
            idCookie.setMaxAge(60 * 60 * 24 * 8);

            Cookie roleCookie = new Cookie("role", String.valueOf(student.getFirst().getRole()));
            roleCookie.setPath("/");
            roleCookie.setHttpOnly(false);
            roleCookie.setMaxAge(60 * 60 * 24 * 8);

            response.addCookie(nameCookie);
            response.addCookie(empCookie);
            response.addCookie(idCookie);
            response.addCookie(roleCookie);

            // 默认跳转到原请求或首页
            response.sendRedirect("http://10.101.162.248:5174/home");
        };
    }

}