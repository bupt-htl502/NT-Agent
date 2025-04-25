package com.coldwindx.server.manager;

import com.coldwindx.server.config.FeishuConfig;
import com.coldwindx.server.entity.form.FeishuWebComponentAuth;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import com.alibaba.fastjson2.JSONObject;

import java.util.*;

@Component
public class FeishuManager {
    @Autowired
    private FeishuConfig config;

    public FeishuWebComponentAuth signature(String url){
        RestTemplate client = new RestTemplate();
        // 1. 通过app_id和app_secret获取app_access_token
        Map<String, Object> params = new HashMap<>();
        params.put("app_id", config.getAPP_ID());
        params.put("app_secret", config.getAPP_SECRET());

        HttpEntity<Object> entity = new HttpEntity<>(params);
        ResponseEntity<String> response = client.exchange(config.getAPP_ACCESS_TOKEN_URI(), HttpMethod.POST, entity, String.class);

        JSONObject jsonAppAccessToken = JSONObject.parseObject(response.getBody());
        String appAccessToken = Objects.requireNonNull(jsonAppAccessToken).getString("app_access_token");

        // 2.通过tenantAccessToken获取ticket
        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + appAccessToken);

        HttpEntity<Object> entity1 = new HttpEntity<>(null, headers);
        response = client.exchange(config.getJSAPI_TICKET_URI(), HttpMethod.POST, entity1, String.class);

        JSONObject jsonTicket = JSONObject.parseObject(response.getBody());
        String ticket = Objects.requireNonNull(jsonTicket).getJSONObject("data").getString("ticket");

        // 3.按照要求拼接Signature,然后使用sha1进行加密
        StringBuilder builder = new StringBuilder();
        Long timestamp = System.currentTimeMillis();
        builder.append("jsapi_ticket=").append(ticket)
                .append("&noncestr=").append(config.getNONCE_STR())
                .append("&timestamp=").append(timestamp)
                .append("&url=").append(url);
        String signature = DigestUtils.sha1Hex(builder.toString());

        List<String> list = new ArrayList<>();
        list.add("DocsComponent");
        return FeishuWebComponentAuth.builder().openid("").signature(signature).appid(config.getAPP_ID()).timestamp(timestamp.toString())
                .noncestr(config.getNONCE_STR()).url(url)
                .jsApiList(list).locale("zh-CN").build();
    }
}
