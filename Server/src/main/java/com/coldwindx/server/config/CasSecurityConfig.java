package com.coldwindx.server.config;

import com.coldwindx.server.service.impl.CustomCasUserDetailsService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.Cookie;
import org.apereo.cas.client.validation.Cas30ProxyTicketValidator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.cas.authentication.CasAssertionAuthenticationToken;
import org.springframework.security.cas.ServiceProperties;
import org.springframework.security.cas.authentication.CasAuthenticationProvider;
import org.springframework.security.cas.web.CasAuthenticationEntryPoint;
import org.springframework.security.cas.web.CasAuthenticationFilter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsByNameServiceWrapper;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;

import java.net.URLEncoder;
import java.util.Map;

@Configuration
@EnableWebSecurity
public class CasSecurityConfig {

    @Resource
    private CustomCasUserDetailsService customCasUserDetailsService;

    @Value("${cas.server-url-prefix}")
    private String casServerUrlPrefix;

    @Value("${cas.server-login-url}")
    private String casServerLoginUrl;

    @Value("${cas.service-url}")
    private String casServiceUrl;


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

    // 配置安全过滤链
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   CasAuthenticationEntryPoint entryPoint,
                                                   CasAuthenticationFilter casFilter) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/").permitAll()
                        .anyRequest().authenticated()
                )
                .exceptionHandling(ex -> ex
                        .authenticationEntryPoint(entryPoint)
                )
                .addFilterBefore(casFilter, LogoutFilter.class);

        return http.build();
    }

    // 配置登出过滤器
    @Bean
    public LogoutFilter logoutFilter() {
        String logoutUrl = casServerUrlPrefix + "/logout?service=" + casServiceUrl;
        org.springframework.security.web.authentication.logout.LogoutFilter logoutFilter =
                new org.springframework.security.web.authentication.logout.LogoutFilter(
                        logoutUrl,
                        new SecurityContextLogoutHandler()
                );
        logoutFilter.setFilterProcessesUrl("/logout/cas");
        return logoutFilter;
    }
//登录成功配置器
    @Bean
    public AuthenticationSuccessHandler casAuthenticationSuccessHandler() {
        return (request, response, authentication) -> {
            // 获取用户名、扩展属性
            CasAssertionAuthenticationToken token = (CasAssertionAuthenticationToken) authentication;
            String username = token.getName();
            Map<String, Object> attributes = token.getAssertion().getPrincipal().getAttributes();
            String name = (String) attributes.get("name");
            String employeeNumber = (String) attributes.get("employeenumber");

            // 可以设置到 Cookie
            Cookie nameCookie = new Cookie("userName", URLEncoder.encode(name, "UTF-8"));
            nameCookie.setPath("/");
            nameCookie.setHttpOnly(false); // 前端JS可访问
            nameCookie.setMaxAge(60 * 60 * 24 * 8); // 8天

            Cookie empCookie = new Cookie("employeeNumber", employeeNumber);
            empCookie.setPath("/");
            empCookie.setHttpOnly(false);
            empCookie.setMaxAge(60 * 60 * 24 * 8);

            response.addCookie(nameCookie);
            response.addCookie(empCookie);

            // 默认跳转到原请求或首页
            response.sendRedirect("/");
        };
    }

}