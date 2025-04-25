package com.coldwindx.server.entity.form;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

@Data
@AllArgsConstructor
public class RespResult implements Serializable {
    public Integer code;
    public String message;
    public Object data;
}
