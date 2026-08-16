package com.provider.util;

public class UserRedisKeyBuilder {

    public static final String USER_KEY_PREFIX = "user:";

    public  String buildUserKey(Long id) {
        return "user:" + id;
    }
}
