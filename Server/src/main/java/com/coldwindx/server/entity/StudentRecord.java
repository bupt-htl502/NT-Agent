package com.coldwindx.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class StudentRecord {
    private List<StudentScoreVo> studentList;
    private List<SceneScoreVo> sceneAverages;
    private ScoreStatisticDto statistics;

    @Data
    public static class ScoreStatisticDto {
        private Integer maxCommitTimes;
    }
}
