package com.ti.live.live.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.ti.live.live.mapper")
@ComponentScan(basePackages = {"com.ti.live.live"})
public class LiveProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(LiveProviderApplication.class, args);
    }
}
