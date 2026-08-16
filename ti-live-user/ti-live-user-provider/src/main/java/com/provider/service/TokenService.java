package com.provider.service;

import jakarta.annotation.Resource;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class TokenService {

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    private static final String TOKEN_KEY_PREFIX = "user:token:";
    private static final int TOKEN_EXPIRE_DAYS = 7;

    public String createAndSaveLoginToken(Long userId) {
        String token = UUID.randomUUID().toString().replace("-", "");
        String redisKey = TOKEN_KEY_PREFIX + token;
        redisTemplate.opsForValue().set(redisKey, String.valueOf(userId), TOKEN_EXPIRE_DAYS, TimeUnit.DAYS);
        return token;
    }

    public Long getUserIdByToken(String token) {
        String redisKey = TOKEN_KEY_PREFIX + token;
        Object userId = redisTemplate.opsForValue().get(redisKey);
        if (userId != null) {
            return Long.valueOf(userId.toString());
        }
        return null;
    }
}
