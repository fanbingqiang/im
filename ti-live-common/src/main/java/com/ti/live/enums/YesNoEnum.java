package com.ti.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum YesNoEnum {

    NO(0, "否"),
    YES(1, "是");

    private final int code;
    private final String description;

    public static YesNoEnum getByCode(int code) {
        for (YesNoEnum val : values()) {
            if (val.getCode() == code) {
                return val;
            }
        }
        return NO;
    }
}
