package com.ti.live.im.service;

import com.ti.live.im.dto.ImRoomDTO;

import java.util.List;

public interface IImRoomService {

    /**
     * 创建房间
     */
    ImRoomDTO createRoom(String roomName);

    /**
     * 进入房间
     */
    void enterRoom(Long roomId, Long userId);

    /**
     * 离开房间
     */
    void leaveRoom(Long roomId, Long userId);

    /**
     * 获取房间信息
     */
    ImRoomDTO getRoomInfo(Long roomId);

    /**
     * 获取在线房间列表
     */
    List<ImRoomDTO> getOnlineRooms();

    /**
     * 获取房间在线人数
     */
    int getOnlineCount(Long roomId);

    /**
     * 关闭房间
     */
    void closeRoom(Long roomId);
}
