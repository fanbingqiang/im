package com.ti.live.im.Service;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@EnableDubbo
@MapperScan("com.ti.live.im.mapper")
@ComponentScan(basePackages = {"com.ti.live.im"})
public class ImProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(ImProviderApplication.class, args);
    }
}
