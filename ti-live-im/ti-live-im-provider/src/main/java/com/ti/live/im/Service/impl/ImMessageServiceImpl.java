package com.ti.live.im.Service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.ti.live.im.dto.ImMessageDTO;
import com.ti.live.im.dto.ImGiftDTO;
import com.ti.live.im.entity.ImMessage;
import com.ti.live.im.mapper.ImMessageMapper;
import com.ti.live.im.service.IImMessageService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@DubboService
public class ImMessageServiceImpl implements IImMessageService {

    @Autowired
    private ImMessageMapper imMessageMapper;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public ImMessageDTO sendMessage(ImMessageDTO message) {
        ImMessage entity = new ImMessage();
        BeanUtils.copyProperties(message, entity);
        entity.setCreateTime(LocalDateTime.now());
        entity.setUpdateTime(LocalDateTime.now());
        entity.setIsRead(0);

        // 处理礼物信息
        if (message.getGift() != null) {
            try {
                entity.setGiftInfo(objectMapper.writeValueAsString(message.getGift()));
            } catch (Exception e) {
                log.error("序列化礼物信息失败", e);
            }
        }

        // 处理弹幕字段
        if (message.getIsBarrage() != null) {
            entity.setIsBarrage(message.getIsBarrage() ? 1 : 0);
        } else {
            entity.setIsBarrage(0);
        }

        imMessageMapper.insert(entity);

        ImMessageDTO result = new ImMessageDTO();
        BeanUtils.copyProperties(entity, result);

        // 反序列化礼物信息
        if (entity.getGiftInfo() != null) {
            try {
                result.setGift(objectMapper.readValue(entity.getGiftInfo(), ImGiftDTO.class));
            } catch (Exception e) {
                log.error("反序列化礼物信息失败", e);
            }
        }

        // 转换弹幕字段
        result.setIsBarrage(entity.getIsBarrage() == 1);

        return result;
    }

    @Override
    public List<ImMessageDTO> batchSendMessages(List<ImMessageDTO> messages) {
        List<ImMessageDTO> results = new ArrayList<>();
        for (ImMessageDTO message : messages) {
            results.add(sendMessage(message));
        }
        return results;
    }

    @Override
    public List<ImMessageDTO> getRoomHistory(Long roomId, int limit) {
        LambdaQueryWrapper<ImMessage> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ImMessage::getRoomId, roomId)
                .orderByDesc(ImMessage::getSendTime)
                .last("LIMIT " + limit);

        List<ImMessage> messages = imMessageMapper.selectList(wrapper);
        List<ImMessageDTO> result = new ArrayList<>();
        for (ImMessage msg : messages) {
            ImMessageDTO dto = new ImMessageDTO();
            BeanUtils.copyProperties(msg, dto);

            // 反序列化礼物信息
            if (msg.getGiftInfo() != null) {
                try {
                    dto.setGift(objectMapper.readValue(msg.getGiftInfo(), ImGiftDTO.class));
                } catch (Exception e) {
                    log.error("反序列化礼物信息失败", e);
                }
            }

            // 转换弹幕字段
            dto.setIsBarrage(msg.getIsBarrage() == 1);

            result.add(dto);
        }
        return result;
    }

    @Override
    public List<ImMessageDTO> getLatestMessages(Long roomId, int count) {
        return getRoomHistory(roomId, count);
    }

    @Override
    public void markAsRead(Long msgId) {
        ImMessage message = new ImMessage();
        message.setMsgId(msgId);
        message.setIsRead(1);
        imMessageMapper.updateById(message);
    }
}
