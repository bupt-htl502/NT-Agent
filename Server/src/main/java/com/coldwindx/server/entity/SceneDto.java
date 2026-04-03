package com.coldwindx.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneDto {
    private Integer id;
    private String label;
    private Integer parent;
    private Integer level;
    private String url;
    private Integer pre_exp;
    private Integer next_exp;
}
