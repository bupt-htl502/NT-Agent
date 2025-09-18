package com.coldwindx.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScoreVo {
    private String studentNo;
    private String name;
    private Double averageScore;
    private Integer sumCommitTimes;
    private Map<String, Double> scores;
    private Map<String, Integer> commitTimes;
}
