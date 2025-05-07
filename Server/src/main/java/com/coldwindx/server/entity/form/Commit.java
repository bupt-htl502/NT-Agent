package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class Commit extends FormParam {
    private Long studentId;
    private Integer sceneId;
    private Double score;
    private String path;
    private Long createTime;
}
