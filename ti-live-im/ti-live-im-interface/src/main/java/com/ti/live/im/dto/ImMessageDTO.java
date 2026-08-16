package com.ti.live.im.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImMessageDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 消息ID
     */
    private Long msgId;

    /**
     * 发送者用户ID
     */
    private Long senderId;

    /**
     * 发送者昵称
     */
    private String senderName;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 消息类型：1-文本 2-图片 3-礼物 4-系统 5-进入房间 6-离开房间
     */
    private Integer msgType;

    /**
     * 消息内容
     */
    private String content;

    /**
     * 礼物信息（当msgType=3时）
     */
    private ImGiftDTO gift;

    /**
     * 弹幕效果（当msgType=1时）
     */
    private Boolean isBarrage;

    /**
     * 发送时间戳
     */
    private Long sendTime;

    /**
     * 是否已读：0-未读 1-已读
     */
    private Integer isRead;
}
