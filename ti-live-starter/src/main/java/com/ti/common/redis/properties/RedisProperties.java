package com.ti.common.redis.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@Data
@ConfigurationProperties(prefix = "ti.redis")
public class RedisProperties {

    private String host = "localhost";

    private int port = 6379;

    private String password;

    private int database = 0;

    private int timeout = 3000;

    private Pool pool = new Pool();

    @Data
    public static class Pool {
        private int maxActive = 8;
        private int maxWait = -1;
        private int maxIdle = 8;
        private int minIdle = 0;
    }
}
