package com.ti.live.live.provider.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.ti.live.live.dto.LiveStreamDTO;
import com.ti.live.live.entity.LiveStream;
import com.ti.live.live.mapper.LiveStreamMapper;
import com.ti.live.live.service.ILiveStreamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

@Slf4j
@Service
@DubboService
public class LiveStreamServiceImpl implements ILiveStreamService {

    @Resource
    private LiveStreamMapper liveStreamMapper;

    @Resource
    private StringRedisTemplate redisTemplate;

    private static final String RTMP_SERVER = "rtmp://localhost:1935/live";
    private static final String HLS_SERVER = "http://localhost:8080/hls";

    @Override
    public LiveStreamDTO createLiveStream(Long anchorId, String title, String roomName) {
        LiveStream liveStream = new LiveStream();
        liveStream.setAnchorId(anchorId);
        liveStream.setAnchorName("主播" + anchorId);
        liveStream.setRoomId(anchorId);
        liveStream.setRoomName(roomName);
        liveStream.setTitle(title);
        liveStream.setCoverUrl("https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=live%20stream%20cover%20with%20blue%20theme&image_size=square_hd");
        
        // 生成推流和拉流地址
        String streamKey = "stream_" + anchorId + "_" + System.currentTimeMillis();
        liveStream.setPushUrl(RTMP_SERVER + "?streamKey=" + streamKey);
        liveStream.setPullUrl(HLS_SERVER + "/" + streamKey + ".m3u8");
        
        liveStream.setStatus(0);
        liveStream.setViewerCount(0);
        liveStream.setCreateTime(LocalDateTime.now());

        liveStreamMapper.insert(liveStream);
        log.info("创建直播流成功: streamId={}, anchorId={}", liveStream.getStreamId(), anchorId);

        return convertToDTO(liveStream);
    }

    @Override
    public LiveStreamDTO startLiveStream(Long streamId) {
        LiveStream liveStream = liveStreamMapper.selectById(streamId);
        if (liveStream == null) {
            throw new RuntimeException("直播流不存在");
        }

        liveStream.setStatus(1);
        liveStream.setStartTime(LocalDateTime.now());
        liveStreamMapper.updateById(liveStream);

        // 缓存直播流信息
        redisTemplate.opsForValue().set("live:stream:" + streamId, String.valueOf(1));

        log.info("开始直播: streamId={}", streamId);
        return convertToDTO(liveStream);
    }

    @Override
    public LiveStreamDTO endLiveStream(Long streamId) {
        LiveStream liveStream = liveStreamMapper.selectById(streamId);
        if (liveStream == null) {
            throw new RuntimeException("直播流不存在");
        }

        liveStream.setStatus(2);
        liveStream.setEndTime(LocalDateTime.now());
        liveStreamMapper.updateById(liveStream);

        // 移除缓存
        redisTemplate.delete("live:stream:" + streamId);

        log.info("结束直播: streamId={}", streamId);
        return convertToDTO(liveStream);
    }

    @Override
    public LiveStreamDTO getLiveStreamInfo(Long streamId) {
        LiveStream liveStream = liveStreamMapper.selectById(streamId);
        if (liveStream == null) {
            throw new RuntimeException("直播流不存在");
        }
        return convertToDTO(liveStream);
    }

    @Override
    public LiveStreamDTO getRoomLiveStream(Long roomId) {
        LambdaQueryWrapper<LiveStream> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveStream::getRoomId, roomId)
                .eq(LiveStream::getStatus, 1) // 直播中
                .orderByDesc(LiveStream::getCreateTime)
                .last("LIMIT 1");

        LiveStream liveStream = liveStreamMapper.selectOne(wrapper);
        return liveStream != null ? convertToDTO(liveStream) : null;
    }

    @Override
    public List<LiveStreamDTO> getLiveStreams() {
        LambdaQueryWrapper<LiveStream> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveStream::getStatus, 1) // 直播中
                .orderByDesc(LiveStream::getStartTime);

        List<LiveStream> liveStreams = liveStreamMapper.selectList(wrapper);
        return liveStreams.stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public List<LiveStreamDTO> getAnchorLiveHistory(Long anchorId, int page, int size) {
        Page<LiveStream> pageInfo = new Page<>(page, size);
        LambdaQueryWrapper<LiveStream> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(LiveStream::getAnchorId, anchorId)
                .orderByDesc(LiveStream::getCreateTime);

        Page<LiveStream> result = liveStreamMapper.selectPage(pageInfo, wrapper);
        return result.getRecords().stream().map(this::convertToDTO).collect(Collectors.toList());
    }

    @Override
    public void updateViewerCount(Long streamId, int count) {
        LiveStream liveStream = liveStreamMapper.selectById(streamId);
        if (liveStream != null) {
            liveStream.setViewerCount(count);
            liveStreamMapper.updateById(liveStream);
            // 缓存观看人数
            redisTemplate.opsForValue().set("live:viewer:" + streamId, String.valueOf(count));
        }
    }

    private LiveStreamDTO convertToDTO(LiveStream liveStream) {
        LiveStreamDTO dto = new LiveStreamDTO();
        BeanUtils.copyProperties(liveStream, dto);
        return dto;
    }
}
