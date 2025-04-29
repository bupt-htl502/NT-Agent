package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Student extends FormParam {
    public String name;
    public String studentNo;
    public Integer role;
    public Integer grade;
}