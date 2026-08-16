package com.ti.live.live.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@TableName("live_stream")
public class LiveStream {
    @TableId(type = IdType.AUTO)
    private Long streamId;

    private Long anchorId;

    private String anchorName;

    private Long roomId;

    private String roomName;

    private String title;

    private String coverUrl;

    private String pushUrl;

    private String pullUrl;

    private Integer status;

    private Integer viewerCount;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;
}
