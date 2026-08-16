package com.ti.live.live.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class LiveStreamDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 直播流ID
     */
    private Long streamId;

    /**
     * 主播ID
     */
    private Long anchorId;

    /**
     * 主播名称
     */
    private String anchorName;

    /**
     * 直播间ID
     */
    private Long roomId;

    /**
     * 直播间名称
     */
    private String roomName;

    /**
     * 直播标题
     */
    private String title;

    /**
     * 直播封面
     */
    private String coverUrl;

    /**
     * 直播推流地址
     */
    private String pushUrl;

    /**
     * 直播拉流地址
     */
    private String pullUrl;

    /**
     * 直播状态：0-准备中 1-直播中 2-已结束
     */
    private Integer status;

    /**
     * 观看人数
     */
    private Integer viewerCount;

    /**
     * 开始时间
     */
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    private LocalDateTime endTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;
}
