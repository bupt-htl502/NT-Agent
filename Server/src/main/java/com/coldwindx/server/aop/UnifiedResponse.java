package com.coldwindx.server.aop;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 统一响应注解<br/>
 * 添加注解后，统一响应体才能生效
 * @author Zhulin
 * @date 2025-04-18
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
public @interface UnifiedResponse {}
