package com.provider.config;

import org.apache.dubbo.config.spring.context.annotation.EnableDubbo;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * 自定义 @EnableDubboClient 注解
 * 用于兼容旧版 Dubbo 代码
 * 实际功能等同于 @EnableDubbo
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
@EnableDubbo
@Import(DubboClientConfig.class)
public @interface EnableDubboClient {
}
