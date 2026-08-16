package com.ti.common.redis.config;

import org.springframework.context.annotation.Configuration;

@Configuration
public class SMSCacheKeyBuilder extends RedisKeyBuilder{
    public static final String SMS_CACHE_KEY_PREFIX = "ssm:login:code:";
    public String buildSMSCacheKey(String mobile) {

        return super.buildKey()+SMS_CACHE_KEY_PREFIX+mobile;
    }
}
