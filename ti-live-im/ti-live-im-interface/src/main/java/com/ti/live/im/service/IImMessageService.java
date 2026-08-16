package com.ti.live.im.service;

import com.ti.live.im.dto.ImMessageDTO;

import java.util.List;

public interface IImMessageService {

    /**
     * 发送消息
     */
    ImMessageDTO sendMessage(ImMessageDTO message);

    /**
     * 批量发送消息
     */
    List<ImMessageDTO> batchSendMessages(List<ImMessageDTO> messages);

    /**
     * 获取房间历史消息
     */
    List<ImMessageDTO> getRoomHistory(Long roomId, int limit);

    /**
     * 获取最新消息
     */
    List<ImMessageDTO> getLatestMessages(Long roomId, int count);

    /**
     * 标记消息已读
     */
    void markAsRead(Long msgId);
}
