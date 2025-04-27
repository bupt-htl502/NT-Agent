package com.coldwindx.server.entity.form;

import com.coldwindx.server.entity.FormParam;
import lombok.Data;

@Data
public class CourseInfo extends FormParam {
    /**
     * 获得文件路径信息
     */
    public String filePath;
    public String studentId;
    public String studentNo;
    public String expScene;
    public String expName;
}
