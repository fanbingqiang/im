package com.ti.live.live.service;

import com.ti.live.live.dto.LiveStreamDTO;

import java.util.List;

public interface ILiveStreamService {

    /**
     * 创建直播流
     */
    LiveStreamDTO createLiveStream(Long anchorId, String title, String roomName);

    /**
     * 开始直播
     */
    LiveStreamDTO startLiveStream(Long streamId);

    /**
     * 结束直播
     */
    LiveStreamDTO endLiveStream(Long streamId);

    /**
     * 获取直播流信息
     */
    LiveStreamDTO getLiveStreamInfo(Long streamId);

    /**
     * 获取直播间的直播流
     */
    LiveStreamDTO getRoomLiveStream(Long roomId);

    /**
     * 获取正在直播的流列表
     */
    List<LiveStreamDTO> getLiveStreams();

    /**
     * 获取主播的直播历史
     */
    List<LiveStreamDTO> getAnchorLiveHistory(Long anchorId, int page, int size);

    /**
     * 更新观看人数
     */
    void updateViewerCount(Long streamId, int count);
}
