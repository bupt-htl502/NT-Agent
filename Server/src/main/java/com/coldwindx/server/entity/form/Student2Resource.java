package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Student2Resource extends FormParam {
    private Long studentId;
    private Integer sceneId;
    private String path;
    private String criterion;
    private Long createTime;

}
