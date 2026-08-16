package com.ti.live.vod.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.ti.live.vod.mapper")
@ComponentScan(basePackages = {"com.ti.live.vod"})
public class VodProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(VodProviderApplication.class, args);
    }
}
