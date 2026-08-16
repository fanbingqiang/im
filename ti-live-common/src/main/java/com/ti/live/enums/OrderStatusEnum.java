package com.ti.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OrderStatusEnum {

    UNPAID(0, "待支付"),
    PAID(1, "已支付"),
    COMPLETED(2, "已完成"),
    CANCELED(3, "已取消"),
    REFUNDED(4, "已退款");

    private final int code;
    private final String description;

    public static OrderStatusEnum getByCode(int code) {
        for (OrderStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return UNPAID;
    }
}
