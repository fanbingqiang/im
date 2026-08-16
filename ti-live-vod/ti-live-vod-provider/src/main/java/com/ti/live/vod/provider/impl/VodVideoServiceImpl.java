package com.ti.live.vod.provider.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ti.live.vod.dto.VodVideoDTO;
import com.ti.live.vod.entity.VodVideo;
import com.ti.live.vod.mapper.VodVideoMapper;
import com.ti.live.vod.service.IVodVideoService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@DubboService
public class VodVideoServiceImpl implements IVodVideoService {

    @Resource
    private VodVideoMapper vodVideoMapper;

    @Resource
    private StringRedisTemplate redisTemplate;

    @Override
    public VodVideoDTO uploadVideo(Long userId, String title, String description, String videoUrl, String coverUrl, Integer duration) {
        VodVideo video = new VodVideo();
        video.setUserId(userId);
        video.setUserName("用户" + userId); // 实际应从用户服务获取
        video.setTitle(title);
        video.setDescription(description);
        video.setVideoUrl(videoUrl);
        video.setCoverUrl(coverUrl);
        video.setDuration(duration);
        video.setPlayCount(0);
        video.setStatus(0); // 待审核
        video.setCreateTime(LocalDateTime.now());
        video.setUpdateTime(LocalDateTime.now());

        vodVideoMapper.insert(video);
        log.info("上传视频成功: videoId={}, userId={}", video.getVideoId(), userId);

        return convertToDTO(video);
    }

    @Override
    public VodVideoDTO getVideoInfo(Long videoId) {
        // 先从缓存获取
        String cacheKey = "vod:video:" + videoId;
        String cachedVideo = redisTemplate.opsForValue().get(cacheKey);
        if (cachedVideo != null) {
            // 缓存命中，解析数据
            // 这里简化处理，实际应该反序列化JSON
        }

        VodVideo video = vodVideoMapper.selectById(videoId);
        if (video == null) {
            throw new RuntimeException("视频不存在");
        }

        // 缓存视频信息
        // redisTemplate.opsForValue().set(cacheKey, JSON.toJSONString(video), 30, TimeUnit.MINUTES);

        return convertToDTO(video);
    }

    @Override
    public VodVideoDTO updateVideo(Long videoId, String title, String description, String tags) {
        VodVideo video = vodVideoMapper.selectById(videoId);
        if (video == null) {
            throw new RuntimeException("视频不存在");
        }

        video.setTitle(title);
        video.setDescription(description);
        video.setTags(tags);
        video.setUpdateTime(LocalDateTime.now());
        vodVideoMapper.updateById(video);

        // 清除缓存
        redisTemplate.delete("vod:video:" + videoId);

        log.info("更新视频信息: videoId={}", videoId);
        return convertToDTO(video);
    }

    @Override
    public void deleteVideo(Long videoId) {
        VodVideo video = vodVideoMapper.selectById(videoId);
        if (video == null) {
            throw new RuntimeException("视频不存在");
        }

        vodVideoMapper.deleteById(videoId);

        // 清除缓存
        redisTemplate.delete("vod:video:" + videoId);

        log.info("删除视频: videoId={}", videoId);
    }

    @Override
    public VodVideoDTO auditVideo(Long videoId, Integer status) {
        VodVideo video = vodVideoMapper.selectById(videoId);
        if (video == null) {
            throw new RuntimeException("视频不存在");
        }

        video.setStatus(status);
        video.setUpdateTime(LocalDateTime.now());
        vodVideoMapper.updateById(video);

        // 清除缓存
        redisTemplate.delete("vod:video:" + videoId);

        log.info("审核视频: videoId={}, status={}", videoId, status);
        return convertToDTO(video);
    }

    @Override
    public void incrementPlayCount(Long videoId) {
        // 使用Redis原子操作增加播放次数
        String playCountKey = "vod:playCount:" + videoId;
        redisTemplate.opsForValue().increment(playCountKey);

        // 定期同步到数据库（这里简化处理，直接更新）
        VodVideo video = vodVideoMapper.selectById(videoId);
        if (video != null) {
            video.setPlayCount(video.getPlayCount() + 1);
            vodVideoMapper.updateById(video);
        }
    }

    @Override
    public List<VodVideoDTO> getVideoList(int page, int size) {
        Page<VodVideo> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<VodVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VodVideo::getStatus, 1) // 已发布
                .orderByDesc(VodVideo::getCreateTime);

        Page<VodVideo> result = vodVideoMapper.selectPage(pageInfo, wrapper);
        return result.getRecords().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<VodVideoDTO> getUserVideos(Long userId, int page, int size) {
        Page<VodVideo> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<VodVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VodVideo::getUserId, userId)
                .orderByDesc(VodVideo::getCreateTime);

        Page<VodVideo> result = vodVideoMapper.selectPage(pageInfo, wrapper);
        return result.getRecords().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<VodVideoDTO> searchVideos(String keyword, int page, int size) {
        Page<VodVideo> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<VodVideo> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(VodVideo::getStatus, 1) // 已发布
                .and(query -> query
                        .like(VodVideo::getTitle, keyword)
                        .or().like(VodVideo::getDescription, keyword)
                        .or().like(VodVideo::getTags, keyword)
                )
                .orderByDesc(VodVideo::getPlayCount)
                .orderByDesc(VodVideo::getCreateTime);

        Page<VodVideo> result = vodVideoMapper.selectPage(pageInfo, wrapper);
        return result.getRecords().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    private VodVideoDTO convertToDTO(VodVideo video) {
        VodVideoDTO dto = new VodVideoDTO();
        BeanUtils.copyProperties(video, dto);
        return dto;
    }
}
