package com.ti.id.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Data
public class BatchIdGenerateResponse implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * ID列表
     */
    private List<IdGenerateResponse> ids;

    /**
     * 生成数量
     */
    private int count;

    /**
     * 业务名称
     */
    private String businessName;
}
