package com.coldwindx.server.entity;

import com.coldwindx.server.entity.enums.ResponseCode;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 业务异常类，继承运行时异常，确保事务正常回滚
 *
 * @author NULL
 * @since  2019-07-16
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class NtAgentException extends RuntimeException{
    /**
     * 状态码
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;

    public NtAgentException(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public NtAgentException(ResponseCode code) {
        this.code = code.getCode();
        this.msg = code.getMsg();
    }

    public NtAgentException(Throwable cause, ResponseCode code) {
        super(cause);
        this.code = code.getCode();
        this.msg = code.getMsg();
    }
}
