package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Student extends FormParam {
    private String name;
    private String studentNo;
    private Integer role;
    private Integer grade;
}