package com.coldwindx.server.entity.form;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
@Builder
public class FeishuWebComponentAuth implements Serializable {
    private String openid;
    private String signature;
    private String appid;
    private String timestamp;
    private String noncestr;
    private String url;
    private List<String> jsApiList;
    private String locale;
}
