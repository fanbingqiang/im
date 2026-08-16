package com.ti.live.im.Service.impl;

import com.ti.live.im.dto.ImRoomDTO;
import com.ti.live.im.service.IImRoomService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Slf4j
@DubboService
public class ImRoomServiceImpl implements IImRoomService {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String ROOM_KEY_PREFIX = "im:room:";
    private static final String ONLINE_KEY_PREFIX = "im:online:";
    private static final String ROOM_LIST_KEY = "im:room:list";
    private static final AtomicLong ROOM_ID_GENERATOR = new AtomicLong(1000);

    private final Map<Long, ImRoomDTO> roomCache = new ConcurrentHashMap<>();

    @Override
    public ImRoomDTO createRoom(String roomName) {
        Long roomId = ROOM_ID_GENERATOR.incrementAndGet();

        ImRoomDTO room = new ImRoomDTO();
        room.setRoomId(roomId);
        room.setRoomName(roomName);
        room.setOnlineCount(0);
        room.setStatus(1);

        roomCache.put(roomId, room);
        stringRedisTemplate.opsForValue().set(ROOM_KEY_PREFIX + roomId, room.getRoomName());
        stringRedisTemplate.opsForSet().add(ROOM_LIST_KEY, String.valueOf(roomId));

        log.info("创建房间成功: roomId={}, roomName={}", roomId, roomName);
        return room;
    }

    @Override
    public void enterRoom(Long roomId, Long userId) {
        String onlineKey = ONLINE_KEY_PREFIX + roomId;
        stringRedisTemplate.opsForSet().add(onlineKey, String.valueOf(userId));

        ImRoomDTO room = roomCache.get(roomId);
        if (room != null) {
            room.setOnlineCount(stringRedisTemplate.opsForSet().size(onlineKey).intValue());
        }
        log.info("用户{}进入房间{}, 在线人数: {}", userId, roomId, getOnlineCount(roomId));
    }

    @Override
    public void leaveRoom(Long roomId, Long userId) {
        String onlineKey = ONLINE_KEY_PREFIX + roomId;
        stringRedisTemplate.opsForSet().remove(onlineKey, String.valueOf(userId));

        ImRoomDTO room = roomCache.get(roomId);
        if (room != null) {
            room.setOnlineCount(stringRedisTemplate.opsForSet().size(onlineKey).intValue());
        }
        log.info("用户{}离开房间{}, 在线人数: {}", userId, roomId, getOnlineCount(roomId));
    }

    @Override
    public ImRoomDTO getRoomInfo(Long roomId) {
        ImRoomDTO room = roomCache.get(roomId);
        if (room != null) {
            room.setOnlineCount(getOnlineCount(roomId));
        }
        return room;
    }

    @Override
    public List<ImRoomDTO> getOnlineRooms() {
        List<ImRoomDTO> rooms = new ArrayList<>();
        for (ImRoomDTO room : roomCache.values()) {
            if (room.getStatus() == 1 && getOnlineCount(room.getRoomId()) > 0) {
                room.setOnlineCount(getOnlineCount(room.getRoomId()));
                rooms.add(room);
            }
        }
        return rooms;
    }

    @Override
    public int getOnlineCount(Long roomId) {
        String onlineKey = ONLINE_KEY_PREFIX + roomId;
        Long size = stringRedisTemplate.opsForSet().size(onlineKey);
        return size != null ? size.intValue() : 0;
    }

    @Override
    public void closeRoom(Long roomId) {
        ImRoomDTO room = roomCache.get(roomId);
        if (room != null) {
            room.setStatus(2);
            stringRedisTemplate.delete(ONLINE_KEY_PREFIX + roomId);
            stringRedisTemplate.opsForSet().remove(ROOM_LIST_KEY, String.valueOf(roomId));
            log.info("房间{}已关闭", roomId);
        }
    }
}
