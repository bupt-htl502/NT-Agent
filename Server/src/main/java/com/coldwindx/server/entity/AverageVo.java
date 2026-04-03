package com.coldwindx.server.entity;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AverageVo {
    private List<StudentScoreVo> studentScore;
    private List<SceneScoreVo> sceneScore;
}
