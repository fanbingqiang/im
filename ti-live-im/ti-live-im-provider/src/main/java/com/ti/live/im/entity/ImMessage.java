package com.ti.live.im.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("im_message")
public class ImMessage {
    @TableId(type = IdType.AUTO)
    private Long msgId;

    private Long senderId;

    private String senderName;

    private Long roomId;

    private Integer msgType;

    private String content;

    /**
     * 礼物信息（JSON格式）
     */
    private String giftInfo;

    /**
     * 是否弹幕消息
     */
    private Integer isBarrage;

    private Long sendTime;

    private Integer isRead;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
