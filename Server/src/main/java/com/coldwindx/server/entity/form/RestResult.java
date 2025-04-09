package com.coldwindx.server.entity.form;

import lombok.Data;

@Data
public class RestResult<T> {
    public Integer code = 0;
    public String message = "success";
    public T data;

    public RestResult(T data){
        this.data = data;
    }
}
