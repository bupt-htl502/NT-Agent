package com.coldwindx.server.aop;

import com.coldwindx.server.entity.form.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice //开启全局注解并且返回json格式数据
public class GlobalExceptionAop {
    @ExceptionHandler(Exception.class) //用于标记需要处理的异常类型
    public RestResult<Object> exceptionHandler(Exception e) {
        log.error("全局异常处理:{}", e.getMessage(), e);
        return new RestResult<>(500, "系统异常，请稍后再试！");
    }
}