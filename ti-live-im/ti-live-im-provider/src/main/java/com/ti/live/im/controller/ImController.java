package com.ti.live.im.controller;

import com.ti.live.im.dto.ImMessageDTO;
import com.ti.live.im.dto.ImRoomDTO;
import com.ti.live.im.service.IImMessageService;
import com.ti.live.im.service.IImRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/im")
public class ImController {

    private final IImMessageService imMessageService;
    private final IImRoomService imRoomService;

    public ImController(IImMessageService imMessageService, IImRoomService imRoomService) {
        this.imMessageService = imMessageService;
        this.imRoomService = imRoomService;
    }

    @PostMapping("/room/create")
    public ImRoomDTO createRoom(@RequestParam String roomName) {
        log.info("创建房间: {}", roomName);
        return imRoomService.createRoom(roomName);
    }

    @GetMapping("/room/list")
    public List<ImRoomDTO> getOnlineRooms() {
        return imRoomService.getOnlineRooms();
    }

    @GetMapping("/room/info")
    public ImRoomDTO getRoomInfo(@RequestParam Long roomId) {
        return imRoomService.getRoomInfo(roomId);
    }

    @GetMapping("/room/onlineCount")
    public int getOnlineCount(@RequestParam Long roomId) {
        return imRoomService.getOnlineCount(roomId);
    }

    @PostMapping("/room/close")
    public void closeRoom(@RequestParam Long roomId) {
        imRoomService.closeRoom(roomId);
    }

    @GetMapping("/message/history")
    public List<ImMessageDTO> getMessageHistory(@RequestParam Long roomId,
                                                 @RequestParam(defaultValue = "50") int limit) {
        return imMessageService.getRoomHistory(roomId, limit);
    }

    @GetMapping("/message/latest")
    public List<ImMessageDTO> getLatestMessages(@RequestParam Long roomId,
                                                 @RequestParam(defaultValue = "20") int count) {
        return imMessageService.getLatestMessages(roomId, count);
    }

    @PostMapping("/message/read")
    public void markAsRead(@RequestParam Long msgId) {
        imMessageService.markAsRead(msgId);
    }
}
