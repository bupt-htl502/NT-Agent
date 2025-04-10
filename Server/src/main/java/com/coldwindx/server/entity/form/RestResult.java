package com.coldwindx.server.entity.form;

import lombok.Data;

@Data
public class RestResult<T> {
    public Integer code = 0;
    public String message = "success";
    public T data = null;

    public RestResult(T data){
        this.data = data;
    }

    public RestResult(int code, String message){
        this.code = code;
        this.message = message;
    }
}
