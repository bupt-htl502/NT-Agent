package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Class extends FormParam {
    private String className;
    private String classCode;
    private String teacherNo;
    private String teacherName;
}
