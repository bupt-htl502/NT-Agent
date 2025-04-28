package com.coldwindx.server.entity.enums;

/**
 * 返回状态码
 *
 * @author NULL
 * @date 2019-07-16
 */
public enum ResponseCode {
    /**
     * 成功返回的状态码
     */
    SUCCESS(0, "success"),
    /**
     * 参数异常
     */
    INVALID_PARAMETERS(10200, "参数非法"),
    /**
     * 资源不存在的状态码
     */
    RESOURCES_NOT_EXIST(10201, "资源不存在"),
    /**
     * 远程服务异常
     */
    REMOTE_SERVICE_ERROR(30000, "远程服务请求异常"),
    /**
     * 所有无法识别的异常默认的返回状态码
     */
    SERVICE_ERROR(50000, "服务器异常");
    /**
     * 状态码
     */
    private int code;
    /**
     * 返回信息
     */
    private String msg;

    ResponseCode(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
