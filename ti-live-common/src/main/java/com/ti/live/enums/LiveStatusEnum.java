package com.ti.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum LiveStatusEnum {

    PREPARING(0, "准备中"),
    LIVING(1, "直播中"),
    ENDED(2, "已结束"),
    CANCELED(3, "已取消");

    private final int code;
    private final String description;

    public static LiveStatusEnum getByCode(int code) {
        for (LiveStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return PREPARING;
    }
}
