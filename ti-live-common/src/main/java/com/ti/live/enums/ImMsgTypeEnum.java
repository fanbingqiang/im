package com.ti.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ImMsgTypeEnum {

    TEXT(1, "文本消息"),
    IMAGE(2, "图片消息"),
    GIFT(3, "礼物消息"),
    SYSTEM(4, "系统通知"),
    ENTER_ROOM(5, "进入房间"),
    LEAVE_ROOM(6, "离开房间");

    private final int code;
    private final String description;

    public static ImMsgTypeEnum getByCode(int code) {
        for (ImMsgTypeEnum type : values()) {
            if (type.getCode() == code) {
                return type;
            }
        }
        return TEXT;
    }
}
