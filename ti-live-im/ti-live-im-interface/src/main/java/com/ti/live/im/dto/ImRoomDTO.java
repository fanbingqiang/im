package com.ti.live.im.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class ImRoomDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * 房间ID
     */
    private Long roomId;

    /**
     * 房间名称
     */
    private String roomName;

    /**
     * 在线人数
     */
    private Integer onlineCount;

    /**
     * 房间状态：0-未开启 1-开启中 2-已结束
     */
    private Integer status;
}
