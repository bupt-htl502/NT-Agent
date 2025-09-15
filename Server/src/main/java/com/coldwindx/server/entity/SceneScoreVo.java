package com.coldwindx.server.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneScoreVo {
    private String chapterName;
    private String taskName;
    private String sceneName;
    private Double averageScore;
    private Double averageCommitTimes;
}
