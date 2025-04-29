package com.coldwindx.server.entity;

import lombok.Data;

@Data
public class QueryParam<T> {
    public T condition;
    public Long offset = 0L;
    public Integer limit = -1;
}