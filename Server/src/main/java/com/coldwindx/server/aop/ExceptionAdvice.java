package com.coldwindx.server.aop;
import com.coldwindx.server.entity.NtAgentException;
import com.coldwindx.server.entity.enums.ResponseCode;
import lombok.extern.slf4j.Slf4j;
import com.coldwindx.server.entity.form.RespResult;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 异常处理器
 *
 * @author Zhulin
 * @since  2025-04-18
 */
@Slf4j
@ResponseBody
@ControllerAdvice(annotations = UnifiedResponse.class)
public class ExceptionAdvice {
    /**
     * 处理未捕获的Exception
     * @param e 异常
     * @return 统一响应体
     */
    @ExceptionHandler(Exception.class)
    public RespResult handleException(Exception e){
        log.error(e.getMessage(),e);
        return new RespResult(ResponseCode.SERVICE_ERROR.getCode(),ResponseCode.SERVICE_ERROR.getMsg(),null);
    }

    /**
     * 处理未捕获的RuntimeException
     * @param e 运行时异常
     * @return 统一响应体
     */
    @ExceptionHandler(RuntimeException.class)
    public RespResult handleRuntimeException(RuntimeException e){
        log.error(e.getMessage(),e);
        return new RespResult(ResponseCode.SERVICE_ERROR.getCode(),ResponseCode.SERVICE_ERROR.getMsg(),null);
    }

    /**
     * 处理业务异常BaseException
     * @param e 业务异常
     * @return 统一响应体
     */
    @ExceptionHandler(NtAgentException.class)
    public RespResult handleBaseException(NtAgentException e){
        log.error(e.getMessage(),e);
        return new RespResult(e.getCode(),e.getMsg(),null);
    }
}
