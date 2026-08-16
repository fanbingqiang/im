package com.ti.live.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ResultCodeEnum {

    SUCCESS(200, "操作成功"),
    FAIL(500, "操作失败"),
    UNAUTHORIZED(401, "未授权"),
    FORBIDDEN(403, "禁止访问"),
    NOT_FOUND(404, "资源不存在"),
    PARAM_ERROR(400, "参数错误"),

    USER_NOT_FOUND(1001, "用户不存在"),
    USER_ALREADY_EXISTS(1002, "用户已存在"),
    TOKEN_INVALID(1003, "Token无效或已过期"),
    SMS_CODE_ERROR(1004, "验证码错误"),
    SMS_CODE_EXPIRED(1005, "验证码已过期"),
    SMS_SEND_FAILED(1006, "短信发送失败"),

    LIVE_NOT_FOUND(2001, "直播不存在"),
    LIVE_STATUS_ERROR(2002, "直播状态异常"),

    GIFT_NOT_FOUND(3001, "礼物不存在"),
    BALANCE_NOT_ENOUGH(3002, "余额不足"),

    IM_CONNECTION_FAILED(4001, "IM连接失败"),
    IM_SEND_FAILED(4002, "消息发送失败");

    private final int code;
    private final String message;
}
