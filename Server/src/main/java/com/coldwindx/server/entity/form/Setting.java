package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Setting extends FormParam {
    private String key;
    private String value;
    private String description;
}
