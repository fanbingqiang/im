package com.ti.id.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class IdGenerateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 业务名称/机器ID标识
     */
    private String businessName;

    /**
     * 是否返回字符串格式（序列化友好）
     */
    private boolean stringFormat;
}
