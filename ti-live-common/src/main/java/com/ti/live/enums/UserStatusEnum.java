package com.ti.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum UserStatusEnum {

    DISABLED(0, "禁用"),
    NORMAL(1, "正常");

    private final int code;
    private final String description;

    public static UserStatusEnum getByCode(int code) {
        for (UserStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        return NORMAL;
    }
}
