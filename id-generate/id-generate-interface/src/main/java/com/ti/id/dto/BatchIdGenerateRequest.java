package com.ti.id.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchIdGenerateRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 业务名称
     */
    private String businessName;

    /**
     * 生成数量
     */
    private int count;

    /**
     * 是否返回字符串格式
     */
    private boolean stringFormat;
}
