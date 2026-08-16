package com.ti.live.vod.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("vod_video")
public class VodVideo {
    @TableId(type = IdType.AUTO)
    private Long videoId;

    private Long userId;

    private String userName;

    private String title;

    private String description;

    private String coverUrl;

    private String videoUrl;

    private Integer duration;

    private Integer playCount;

    private Integer status;

    private String tags;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
