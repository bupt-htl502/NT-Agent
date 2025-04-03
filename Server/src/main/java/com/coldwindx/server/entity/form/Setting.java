package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Setting extends FormParam {
    public String key;
    public String value;
    public String description;
}
