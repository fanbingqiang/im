package com.ti.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum SmsSceneEnum {

    LOGIN(1, "登录"),
    REGISTER(2, "注册"),
    FORGET_PASSWORD(3, "忘记密码"),
    BIND_PHONE(4, "绑定手机号");

    private final int code;
    private final String description;

    public static SmsSceneEnum getByCode(int code) {
        for (SmsSceneEnum scene : values()) {
            if (scene.getCode() == code) {
                return scene;
            }
        }
        return null;
    }
}
