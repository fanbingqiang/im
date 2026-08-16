package com.ti.live.im.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

@Slf4j
@Component
public class WebSocketHandshakeInterceptor implements HandshakeInterceptor {

    private final WebSocketAuthValidator authValidator;

    public WebSocketHandshakeInterceptor(WebSocketAuthValidator authValidator) {
        this.authValidator = authValidator;
    }

    @Override
    public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response,
                                   WebSocketHandler wsHandler, Map<String, Object> attributes) {
        String query = request.getURI().getQuery();
        log.info("WebSocket握手请求, query={}", query);

        if (query == null) {
            log.warn("WebSocket握手失败: 缺少请求参数");
            return false;
        }

        String token = null;
        String userIdStr = null;
        String roomIdStr = null;

        String[] params = query.split("&");
        for (String param : params) {
            String[] kv = param.split("=");
            if (kv.length == 2) {
                switch (kv[0]) {
                    case "token":
                        token = kv[1];
                        break;
                    case "userId":
                        userIdStr = kv[1];
                        break;
                    case "roomId":
                        roomIdStr = kv[1];
                        break;
                }
            }
        }

        WebSocketAuthValidator.AuthResult result = authValidator.validate(token, userIdStr, roomIdStr);
        if (!result.isSuccess()) {
            log.warn("WebSocket握手失败: {}", result.getMessage());
            return false;
        }

        attributes.put("userId", result.getUserId());
        attributes.put("roomId", result.getRoomId());
        attributes.put("token", result.getToken());

        log.info("WebSocket握手成功: userId={}, roomId={}", result.getUserId(), result.getRoomId());
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request, ServerHttpResponse response,
                               WebSocketHandler wsHandler, Exception exception) {
        if (exception != null) {
            log.error("WebSocket握手失败", exception);
        }
    }
}
