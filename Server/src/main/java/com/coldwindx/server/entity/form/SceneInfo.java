package com.coldwindx.server.entity.form;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SceneInfo {
    private Integer sceneId;
    private String chapterName;
    private String taskName;
    private String sceneName;
}
