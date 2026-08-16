package com.ti.live.im.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.ti.live.im.config.ImRoomSessionManager;
import com.ti.live.im.dto.ImGiftDTO;
import com.ti.live.im.dto.ImMessageDTO;
import com.ti.live.im.service.IImMessageService;
import com.ti.live.im.service.IImRoomService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class ChatWebSocketHandler extends TextWebSocketHandler {

    private final ImRoomSessionManager sessionManager;
    private final IImMessageService messageService;
    private final IImRoomService roomService;
    private final ObjectMapper objectMapper = new ObjectMapper();

    public ChatWebSocketHandler(ImRoomSessionManager sessionManager,
                                IImMessageService messageService,
                                IImRoomService roomService) {
        this.sessionManager = sessionManager;
        this.messageService = messageService;
        this.roomService = roomService;
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        Long userId = getLongFromAttribute(session, "userId");
        Long roomId = getLongFromAttribute(session, "roomId");

        if (userId == null || roomId == null) {
            log.warn("WebSocket连接缺少userId或roomId, 关闭连接");
            closeSession(session, CloseStatus.BAD_DATA);
            return;
        }

        sessionManager.addSession(roomId, userId, session);

        // 发送系统消息：用户进入房间
        sendSystemMessage(roomId, null, "用户进入房间");

        // 获取历史消息发送给新用户
        sendHistoryMessages(session, roomId);

        log.info("WebSocket连接建立: userId={}, roomId={}", userId, roomId);
    }

    @Override
    protected void handleTextMessage(WebSocketSession session, TextMessage message) {
        Long userId = getLongFromAttribute(session, "userId");
        Long roomId = getLongFromAttribute(session, "roomId");

        if (userId == null || roomId == null) {
            return;
        }

        try {
            Map<String, Object> msgMap = objectMapper.readValue(message.getPayload(), Map.class);
            String content = (String) msgMap.get("content");
            Integer msgType = msgMap.get("msgType") != null ? ((Number) msgMap.get("msgType")).intValue() : 1;
            Boolean isBarrage = msgMap.get("isBarrage") != null ? (Boolean) msgMap.get("isBarrage") : false;

            ImMessageDTO imMessage = new ImMessageDTO();
            imMessage.setSenderId(userId);
            imMessage.setRoomId(roomId);
            imMessage.setMsgType(msgType);
            imMessage.setSendTime(System.currentTimeMillis());
            imMessage.setIsBarrage(isBarrage);

            // 处理不同类型的消息
            if (msgType == 1) {
                if (content == null || content.trim().isEmpty()) {
                    return;
                }
                imMessage.setContent(content.trim());
            } else if (msgType == 3) {
                //gift
                Map<String, Object> giftMap = (Map<String, Object>) msgMap.get("gift");
                if (giftMap != null) {
                    ImGiftDTO gift = new ImGiftDTO();
                    gift.setGiftId(giftMap.get("giftId") != null ? Long.parseLong(giftMap.get("giftId").toString()) : null);
                    gift.setGiftName((String) giftMap.get("giftName"));
                    gift.setPrice(giftMap.get("price") != null ? ((Number) giftMap.get("price")).intValue() : 0);
                    gift.setCount(giftMap.get("count") != null ? ((Number) giftMap.get("count")).intValue() : 1);
                    gift.setImageUrl((String) giftMap.get("imageUrl"));
                    gift.setAnimationUrl((String) giftMap.get("animationUrl"));
                    imMessage.setGift(gift);
                    imMessage.setContent("赠送了礼物：" + gift.getGiftName());
                }
            }

            // 保存到数据库
            ImMessageDTO savedMessage = messageService.sendMessage(imMessage);

            // 广播给房间内其他人
            String jsonMessage = objectMapper.writeValueAsString(savedMessage);
            sessionManager.broadcast(roomId, jsonMessage, userId);
        } catch (Exception e) {
            log.error("处理消息异常", e);
            sendError(session, "消息发送失败");
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        Long userId = getLongFromAttribute(session, "userId");
        Long roomId = getLongFromAttribute(session, "roomId");

        if (userId != null && roomId != null) {
            sessionManager.removeSession(roomId, userId);
            roomService.leaveRoom(roomId, userId);

            // 发送系统消息：用户离开房间
            sendSystemMessage(roomId, null, "用户离开房间");

            log.info("WebSocket连接关闭: userId={}, roomId={}, status={}", userId, roomId, status);
        }
    }

    @Override
    public void handleTransportError(WebSocketSession session, Throwable exception) {
        Long userId = getLongFromAttribute(session, "userId");
        log.error("WebSocket传输异常: userId={}", userId, exception);
    }

    /**
     * 发送系统消息
     */
    private void sendSystemMessage(Long roomId, Long senderId, String content) {
        try {
            ImMessageDTO systemMsg = new ImMessageDTO();
            systemMsg.setSenderId(senderId);
            systemMsg.setSenderName("系统");
            systemMsg.setRoomId(roomId);
            systemMsg.setMsgType(4);
            systemMsg.setContent(content);
            systemMsg.setSendTime(System.currentTimeMillis());
            systemMsg.setIsRead(1);

            String jsonMessage = objectMapper.writeValueAsString(systemMsg);
            sessionManager.broadcast(roomId, jsonMessage, null);
        } catch (Exception e) {
            log.error("发送系统消息失败", e);
        }
    }

    /**
     * 发送历史消息给新用户
     */
    private void sendHistoryMessages(WebSocketSession session, Long roomId) {
        try {
            java.util.List<ImMessageDTO> historyMessages = messageService.getLatestMessages(roomId, 50);
            for (ImMessageDTO msg : historyMessages) {
                String json = objectMapper.writeValueAsString(msg);
                session.sendMessage(new TextMessage(json));
            }
        } catch (Exception e) {
            log.error("发送历史消息失败", e);
        }
    }

    /**
     * 发送错误消息
     */
    private void sendError(WebSocketSession session, String errorMsg) {
        try {
            Map<String, Object> error = new HashMap<>();
            error.put("type", "error");
            error.put("message", errorMsg);
            error.put("timestamp", System.currentTimeMillis());
            session.sendMessage(new TextMessage(objectMapper.writeValueAsString(error)));
        } catch (Exception e) {
            log.error("发送错误消息失败", e);
        }
    }

    private Long getLongFromAttribute(WebSocketSession session, String key) {
        Object value = session.getAttributes().get(key);
        if (value != null) {
            try {
                return Long.parseLong(value.toString());
            } catch (NumberFormatException e) {
                return null;
            }
        }
        return null;
    }

    private void closeSession(WebSocketSession session, CloseStatus status) {
        try {
            session.close(status);
        } catch (Exception e) {
            log.error("关闭WebSocket会话失败", e);
        }
    }
}
