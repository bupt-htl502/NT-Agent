package com.coldwindx.server.utils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class EvaluateUtils {
    public  String comment(Map<String, Integer> errorMap){
        List<Map.Entry<String, Integer>> topErrors = errorMap.entrySet().stream()
                .filter(e -> e.getValue() > 0) // 只保留有错误的项
                .sorted((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()))
                .limit(3)
                .toList();
        String suggestion;
        if (topErrors.isEmpty()) {
            suggestion = "本次提交中的所有样本均未出现显著误差，继续保持优秀的表现！";
        } else {
            String detail = topErrors.stream()
                    .map(e -> String.format("「%s」出现了 %d 次偏差", e.getKey(), e.getValue()))
                    .collect(Collectors.joining("，"));
            suggestion = "以下样本存在较多误差，请重点关注：\n" + detail + "。";
        }
        return suggestion;
    }
}
