package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Memory extends FormParam {
    public Long userId = 0L;
    public Long topicId = 0L;
    public Integer role = 0;
    public String context = "";
    public Long createTime = 0L;
}