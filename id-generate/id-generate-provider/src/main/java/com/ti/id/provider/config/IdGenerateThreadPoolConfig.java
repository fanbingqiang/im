package com.ti.id.provider.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.*;

@Slf4j
@Configuration
public class IdGenerateThreadPoolConfig {

    @Bean(name = "idGenerateExecutor", destroyMethod = "shutdown")
    public ExecutorService idGenerateExecutor() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 2;
        int maxPoolSize = corePoolSize * 2;
        long keepAliveTime = 60L;

        ThreadPoolExecutor executor = new ThreadPoolExecutor(
                corePoolSize,
                maxPoolSize,
                keepAliveTime,
                TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(10000),
                new ThreadFactory() {
                    private int count = 0;
                    @Override
                    public Thread newThread(Runnable r) {
                        Thread thread = new Thread(r);
                        thread.setName("id-generate-pool-" + count++);
                        thread.setDaemon(false);
                        return thread;
                    }
                },
                new ThreadPoolExecutor.CallerRunsPolicy()
        );

        log.info("ID生成线程池初始化完成: corePoolSize={}, maxPoolSize={}", corePoolSize, maxPoolSize);
        return executor;
    }
}
