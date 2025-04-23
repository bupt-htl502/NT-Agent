package com.coldwindx.server.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Getter
@Configuration
public class FeishuConfig {

    @Value("${feishu.app-id}")
    private String APP_ID;
    @Value("${feishu.app-secret}")
    private String APP_SECRET;

    /** 获取ticket参数的api */
    @Value("${feishu.jsapi-ticket-uri}")
    private String JSAPI_TICKET_URI;

    /** 加签sha1的一个随机参数 */
    @Value("${feishu.nonce-str}")
    private String NONCE_STR;

    /** 用appId和appSecret获取app_access_token(应用级别的授权(只允许查看,不允许编辑,分享等功能权限)) */
    @Value("${feishu.app-access-token-uri}")
    private String APP_ACCESS_TOKEN_URI;

    /** 用户信息接口路径 */
    @Value("${feishu.user-info-uri}")
    private String USER_INFO_URI;

}
