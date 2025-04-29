package com.coldwindx.server.entity.enums;
/**
 * 统计特征顺序
 */
public enum StaticFeature {
    DURATION(0, "duration"),
    PKT_NUM(1, "pkt_num"),
    BYTES(2, "bytes"),
    MAX_PKT_SZ(3, "max_pkt_sz"),
    MIN_PKT_SZ(4, "min_pkt_sz"),
    AVG_PKT_SZ(5, "avg_pkt_sz"),
    MAX_TIME_INTERVAL(6, "max_time_interval"),
    MIN_TIME_INTERVAL(7, "min_time_interval"),
    AVG_TIME_INTERVAL(8, "avg_time_interval"),
    PKT_RATE(9, "pkt_rate"),
    BYTE_RATE(10, "byte_rate"),
    ACK_NUM(11, "ack_num"),
    PSH_NUM(12, "psh_num");
    private int code;
    /**
     * 返回信息
     */
    private String msg;

    StaticFeature(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}

