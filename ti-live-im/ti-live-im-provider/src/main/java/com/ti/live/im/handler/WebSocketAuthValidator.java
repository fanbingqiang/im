package com.ti.live.im.handler;

import com.ti.IUserRPCService;
import lombok.extern.slf4j.Slf4j;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class WebSocketAuthValidator {

    @DubboReference
    private IUserRPCService userRPCService;

    public AuthResult validate(String token, String userIdStr, String roomIdStr) {
        if (token == null || token.isEmpty()) {
            return AuthResult.fail("缺少token");
        }
        if (userIdStr == null || userIdStr.isEmpty() || roomIdStr == null || roomIdStr.isEmpty()) {
            return AuthResult.fail("缺少userId或roomId");
        }

        try {
            Long tokenUserId = userRPCService.getUserIdByToken(token);
            if (tokenUserId == null) {
                return AuthResult.fail("token无效");
            }

            Long userId = Long.parseLong(userIdStr);
            if (!tokenUserId.equals(userId)) {
                return AuthResult.fail("userId与token不匹配");
            }

            return AuthResult.success(userIdStr, roomIdStr, token);
        } catch (NumberFormatException e) {
            return AuthResult.fail("userId格式错误");
        } catch (Exception e) {
            log.error("WebSocket鉴权异常", e);
            return AuthResult.fail("鉴权异常: " + e.getMessage());
        }
    }

    public static class AuthResult {
        private boolean success;
        private String message;
        private String userId;
        private String roomId;
        private String token;

        public static AuthResult success(String userId, String roomId, String token) {
            AuthResult r = new AuthResult();
            r.success = true;
            r.userId = userId;
            r.roomId = roomId;
            r.token = token;
            return r;
        }

        public static AuthResult fail(String message) {
            AuthResult r = new AuthResult();
            r.success = false;
            r.message = message;
            return r;
        }

        public boolean isSuccess() { return success; }
        public String getMessage() { return message; }
        public String getUserId() { return userId; }
        public String getRoomId() { return roomId; }
        public String getToken() { return token; }
    }
}
