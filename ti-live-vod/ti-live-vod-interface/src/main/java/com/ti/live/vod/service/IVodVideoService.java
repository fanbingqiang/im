package com.ti.live.vod.service;

import com.ti.live.vod.dto.VodVideoDTO;

import java.util.List;

public interface IVodVideoService {

    /**
     * 上传视频
     */
    VodVideoDTO uploadVideo(Long userId, String title, String description, String videoUrl, String coverUrl, Integer duration);

    /**
     * 获取视频信息
     */
    VodVideoDTO getVideoInfo(Long videoId);

    /**
     * 更新视频信息
     */
    VodVideoDTO updateVideo(Long videoId, String title, String description, String tags);

    /**
     * 删除视频
     */
    void deleteVideo(Long videoId);

    /**
     * 审核视频
     */
    VodVideoDTO auditVideo(Long videoId, Integer status);

    /**
     * 增加播放次数
     */
    void incrementPlayCount(Long videoId);

    /**
     * 获取视频列表
     */
    List<VodVideoDTO> getVideoList(int page, int size);

    /**
     * 获取用户上传的视频
     */
    List<VodVideoDTO> getUserVideos(Long userId, int page, int size);

    /**
     * 搜索视频
     */
    List<VodVideoDTO> searchVideos(String keyword, int page, int size);
}
