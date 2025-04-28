package com.coldwindx.server.entity.form;

import java.util.HashMap;
import java.util.Map;

public class ResultAndFeatures {
    private Map<String, Integer> Features_pos_map = new HashMap<String, Integer>();
    private String[] Features_list;
    private Map<String, double[]> double_map;
    private Map<String, String[]> string_map;
    public ResultAndFeatures(String[] Features_list, Map<String, double[]> double_map) {
        this.Features_list = Features_list;
        for (int i = 1;i<Features_list.length;i++) {
            String key = Features_list[i];
            Features_pos_map.put(key, i);
        }
        this.double_map = double_map;
    }
    public ResultAndFeatures(String[] Features_list, Map<String, String[]> string_map,boolean isString) {
        this.Features_list = Features_list;
        for (int i = 1;i<Features_list.length;i++) {
            String key = Features_list[i];
            Features_pos_map.put(key, i);
        }
        this.string_map = string_map;
    }
    public Map<String, String[]> getStringMap(){
        return  string_map;
    }
    public Map<String, double[]> getDoubleMap(){
        return  double_map;
    }
    public Map<String, Integer> GetFeaturesPosMap() {
        return  Features_pos_map;
    }
    public  String[] GetFeaturesList(){
        return  Features_list;
    }
}

