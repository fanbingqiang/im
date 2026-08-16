package com.ti.id.provider;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@EnableDubbo
public class IdGenerateProviderApplication {
    public static void main(String[] args) {
        SpringApplication.run(IdGenerateProviderApplication.class, args);
    }
}
