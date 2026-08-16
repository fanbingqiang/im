package com.it.live.controller;

import com.it.live.entity.WebResDTO;
import com.ti.live.im.dto.ImMessageDTO;
import com.ti.live.im.dto.ImRoomDTO;
import com.ti.live.im.service.IImMessageService;
import com.ti.live.im.service.IImRoomService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/im")
public class ImController {

    @DubboReference
    private IImMessageService imMessageService;

    @DubboReference
    private IImRoomService imRoomService;

    @PostMapping("/room/create")
    public WebResDTO createRoom(@RequestParam String roomName) {
        try {
            ImRoomDTO room = imRoomService.createRoom(roomName);
            return WebResDTO.success(room);
        } catch (Exception e) {
            return WebResDTO.error("创建房间失败: " + e.getMessage());
        }
    }

    @GetMapping("/room/list")
    public WebResDTO getOnlineRooms() {
        try {
            List<ImRoomDTO> rooms = imRoomService.getOnlineRooms();
            return WebResDTO.success(rooms);
        } catch (Exception e) {
            return WebResDTO.error("获取房间列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/room/info")
    public WebResDTO getRoomInfo(@RequestParam Long roomId) {
        try {
            ImRoomDTO room = imRoomService.getRoomInfo(roomId);
            return WebResDTO.success(room);
        } catch (Exception e) {
            return WebResDTO.error("获取房间信息失败: " + e.getMessage());
        }
    }

    @GetMapping("/room/onlineCount")
    public WebResDTO getOnlineCount(@RequestParam Long roomId) {
        try {
            int count = imRoomService.getOnlineCount(roomId);
            return WebResDTO.success(count);
        } catch (Exception e) {
            return WebResDTO.error("获取在线人数失败: " + e.getMessage());
        }
    }

    @PostMapping("/room/close")
    public WebResDTO closeRoom(@RequestParam Long roomId) {
        try {
            imRoomService.closeRoom(roomId);
            return WebResDTO.success("房间已关闭");
        } catch (Exception e) {
            return WebResDTO.error("关闭房间失败: " + e.getMessage());
        }
    }

    @GetMapping("/message/history")
    public WebResDTO getMessageHistory(@RequestParam Long roomId,
                                       @RequestParam(defaultValue = "50") int limit) {
        try {
            List<ImMessageDTO> messages = imMessageService.getRoomHistory(roomId, limit);
            return WebResDTO.success(messages);
        } catch (Exception e) {
            return WebResDTO.error("获取历史消息失败: " + e.getMessage());
        }
    }

    @GetMapping("/message/latest")
    public WebResDTO getLatestMessages(@RequestParam Long roomId,
                                       @RequestParam(defaultValue = "20") int count) {
        try {
            List<ImMessageDTO> messages = imMessageService.getLatestMessages(roomId, count);
            return WebResDTO.success(messages);
        } catch (Exception e) {
            return WebResDTO.error("获取最新消息失败: " + e.getMessage());
        }
    }

    @PostMapping("/message/read")
    public WebResDTO markAsRead(@RequestParam Long msgId) {
        try {
            imMessageService.markAsRead(msgId);
            return WebResDTO.success("已标记为已读");
        } catch (Exception e) {
            return WebResDTO.error("标记已读失败: " + e.getMessage());
        }
    }
}
