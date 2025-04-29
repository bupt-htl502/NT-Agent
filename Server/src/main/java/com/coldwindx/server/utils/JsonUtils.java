package com.coldwindx.server.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public class JsonUtils {

    public static final ObjectMapper objectMapper = new ObjectMapper();


    /**
     * json字符串转驼峰
     *
     * @param json String
     * @return String
     */
    public static String convertToCamelCaseJson(String json) throws JsonProcessingException {
        JsonNode jsonNode = objectMapper.readTree(json);

        jsonNode = convertKeysToCamelCase(jsonNode);

        return objectMapper.writeValueAsString(jsonNode);
    }


    /**
     * JsonNode的key转驼峰
     *
     * @param jsonNode JsonNode
     * @return JsonNode
     */
    public static JsonNode convertKeysToCamelCase(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            jsonNode.fields().forEachRemaining(entry -> {
                objectNode.set(snakeToCamel(entry.getKey()), convertKeysToCamelCase(entry.getValue()));
            });
            return objectNode;
        }

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            jsonNode.elements().forEachRemaining(entry -> {
                arrayNode.add(convertKeysToCamelCase(entry));
            });

            return arrayNode;
        }

        return jsonNode;
    }

    public static JsonNode convertKeysToSnake(JsonNode jsonNode) {
        if (jsonNode.isObject()) {
            ObjectNode objectNode = objectMapper.createObjectNode();
            jsonNode.fields().forEachRemaining(entry -> {
                objectNode.set(camelToSnake(entry.getKey()), convertKeysToCamelCase(entry.getValue()));
            });
            return objectNode;
        }

        if (jsonNode.isArray()) {
            ArrayNode arrayNode = objectMapper.createArrayNode();
            jsonNode.elements().forEachRemaining(entry -> {
                arrayNode.add(convertKeysToSnake(entry));
            });

            return arrayNode;
        }

        return jsonNode;
    }


    /**
     * 下划线转驼峰
     *
     * @param key String
     * @return String
     */
    public static String snakeToCamel(String key) {
        if (key.indexOf("_") != 0) {
            String[] s = key.split("_");
            StringBuilder stringBuilder = new StringBuilder(s[0]);
            for (int i = 1; i < s.length; i++) {
                stringBuilder.append(s[i].substring(0, 1).toUpperCase()).
                        append(s[i].substring(1));
            }
            return stringBuilder.toString();
        }

        return key;
    }


    /**
     * 驼峰转下划线
     * @param key String
     * @return String
     */
    public static String camelToSnake(String key) {
        int length = key.length();
        StringBuilder sb = new StringBuilder(length);
        for (int i = 0; i < length; i++) {
            char current = key.charAt(i);
            if (Character.isUpperCase(current)) {
                if (i > 0) {
                    sb.append('_');
                }
                sb.append(Character.toLowerCase(current));
                continue;
            }
            sb.append(current);
        }
        return sb.toString();
    }


}