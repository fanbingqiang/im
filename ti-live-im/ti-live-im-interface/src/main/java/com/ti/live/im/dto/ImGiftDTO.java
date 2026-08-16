package com.ti.live.im.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImGiftDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 礼物ID
     */
    private Long giftId;

    /**
     * 礼物名称
     */
    private String giftName;

    /**
     * 礼物价格（虚拟币）
     */
    private Integer price;

    /**
     * 礼物图片
     */
    private String imageUrl;

    /**
     * 礼物动画效果
     */
    private String animationUrl;

    /**
     * 礼物数量
     */
    private Integer count;
}
