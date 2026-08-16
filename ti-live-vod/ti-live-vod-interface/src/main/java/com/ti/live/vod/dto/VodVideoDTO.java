package com.ti.live.vod.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class VodVideoDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 视频ID
     */
    private Long videoId;

    /**
     * 上传用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String userName;

    /**
     * 视频标题
     */
    private String title;

    /**
     * 视频描述
     */
    private String description;

    /**
     * 视频封面
     */
    private String coverUrl;

    /**
     * 视频地址
     */
    private String videoUrl;

    /**
     * 视频时长（秒）
     */
    private Integer duration;

    /**
     * 播放次数
     */
    private Integer playCount;

    /**
     * 状态：0-待审核 1-已发布 2-审核失败
     */
    private Integer status;

    /**
     * 标签
     */
    private String tags;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;
}
