package com.coldwindx.server.aop;

import com.coldwindx.server.entity.form.RestResult;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;

@Slf4j
@Aspect
@Component
public class HttpResponseAop {
    /**
     * 切面方法
     * 封装函数返回值
     */
    @Pointcut("execution(public * com.coldwindx.server.controller..*(..))")
    public void wrapResponse() {
        log.info("Wrap HttpResponse");
    }

    /**
     * 环切方法
     *
     * @param proceedingJoinPoint
     * @return
     */
    @Around(value = "wrapResponse()")
    public RestResult<Object> doAround(ProceedingJoinPoint proceedingJoinPoint) {
        try {
            //获取方法的执行结果
            Object obj = proceedingJoinPoint.proceed();
            //构建返回对象
            return new RestResult<>(obj);
        } catch (Throwable th) {
            log.error(th.getMessage(), th);
            //构建异常的放回对象
            return new RestResult<>(500, "服务器内部错误，请联系系统管理员！");
        }
    }
}