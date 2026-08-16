package com.ti.id.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdGenerateResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID长整型值
     */
    private long id;

    /**
     * ID字符串值（用于序列化传输）
     */
    private String idStr;

    /**
     * 是否序列化格式
     */
    private boolean stringFormat;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 生成时间戳
     */
    private long timestamp;
}
