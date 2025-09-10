package com.coldwindx.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ScoreVo {
    private String studentId;
    private String studentNo;
    private String name;
    private Double averageScore;
    private Integer commitTimes;
}
