package com.coldwindx.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentScoreVo {
    private String studentNo;
    private String name;
    private Double averageScore;
    private Integer commitTimes;
    private List<ScorePerScene> scores;

    @Data
    public static class ScorePerScene {
        private String chapterName;
        private String taskName;
        private String sceneName;
        private Double score;
        private Integer commitTime;
    }
}
