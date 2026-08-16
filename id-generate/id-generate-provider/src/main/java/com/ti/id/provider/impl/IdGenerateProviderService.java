package com.ti.id.provider.impl;

import com.ti.id.dto.*;
import com.ti.id.service.IIdGenerateService;
import lombok.extern.slf4j.Slf4j;
import me.ahoo.cosid.CosIdGenerator;
import me.ahoo.cosid.StringCosIdGenerator;
import me.ahoo.cosid.provider.CosIdProvider;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Slf4j
@DubboService
public class IdGenerateProviderService implements IIdGenerateService {

    @Autowired
    @Qualifier("idGenerateExecutor")
    private ExecutorService executorService;

    private CosIdGenerator getCosIdGenerator(String businessName) {
        return CosIdProvider.getCosIdGeneratorProvider()
                .getCosIdGenerator(businessName)
                .orElseThrow(() -> new RuntimeException("未找到业务名称对应的ID生成器: " + businessName));
    }

    private StringCosIdGenerator getStringCosIdGenerator(String businessName) {
        return CosIdProvider.getCosIdGeneratorProvider()
                .getStringCosIdGenerator(businessName)
                .orElseThrow(() -> new RuntimeException("未找到业务名称对应的字符串ID生成器: " + businessName));
    }

    @Override
    public IdGenerateResponse generateId(IdGenerateRequest request) {
        log.info("开始生成ID, businessName={}, stringFormat={}", request.getBusinessName(), request.isStringFormat());

        String businessName = request.getBusinessName();
        IdGenerateResponse response = new IdGenerateResponse();
        response.setBusinessName(businessName);

        if (request.isStringFormat()) {
            StringCosIdGenerator stringGenerator = getStringCosIdGenerator(businessName);
            String idStr = stringGenerator.generateAsString();
            response.setIdStr(idStr);
            response.setStringFormat(true);
            log.info("生成字符串ID成功: {}", idStr);
        } else {
            CosIdGenerator generator = getCosIdGenerator(businessName);
            long id = generator.generateAsLong();
            response.setId(id);
            response.setStringFormat(false);
            log.info("生成数字ID成功: {}", id);
        }

        response.setTimestamp(System.currentTimeMillis());
        return response;
    }

    @Override
    public BatchIdGenerateResponse batchGenerateId(BatchIdGenerateRequest request) {
        log.info("开始批量生成ID, businessName={}, count={}", request.getBusinessName(), request.getCount());

        int count = Math.min(request.getCount(), 1000);
        String businessName = request.getBusinessName();
        boolean stringFormat = request.isStringFormat();

        List<Future<IdGenerateResponse>> futures = new ArrayList<>();
        List<IdGenerateResponse> results = new ArrayList<>();

        for (int i = 0; i < count; i++) {
            Future<IdGenerateResponse> future = executorService.submit(() -> {
                IdGenerateRequest req = new IdGenerateRequest();
                req.setBusinessName(businessName);
                req.setStringFormat(stringFormat);
                return generateId(req);
            });
            futures.add(future);
        }

        for (Future<IdGenerateResponse> future : futures) {
            try {
                results.add(future.get(5, TimeUnit.SECONDS));
            } catch (Exception e) {
                log.error("批量生成ID失败", e);
                throw new RuntimeException("批量生成ID失败", e);
            }
        }

        BatchIdGenerateResponse response = new BatchIdGenerateResponse();
        response.setIds(results);
        response.setCount(results.size());
        response.setBusinessName(businessName);

        log.info("批量生成ID完成, 数量={}", results.size());
        return response;
    }

    @Override
    public String generateIdAsString(String businessName) {
        StringCosIdGenerator generator = getStringCosIdGenerator(businessName);
        String id = generator.generateAsString();
        log.info("生成字符串ID成功: {}, businessName={}", id, businessName);
        return id;
    }

    @Override
    public long generateIdAsLong(String businessName) {
        CosIdGenerator generator = getCosIdGenerator(businessName);
        long id = generator.generateAsLong();
        log.info("生成数字ID成功: {}, businessName={}", id, businessName);
        return id;
    }

    @Override
    public String generateSequentialOrderId(String prefix) {
        // 生成序列化订单ID: 前缀 + 年月日时分秒 + 6位随机数
        String timestamp = java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        String random = String.format("%06d", (int) (Math.random() * 1000000));
        String orderId = prefix + timestamp + random;
        log.info("生成序列化订单ID成功: {}", orderId);
        return orderId;
    }

    @Override
    public String generateRandomOrderId(String prefix) {
        // 生成无规律订单ID: 前缀 + 32位随机字符串
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
        StringBuilder sb = new StringBuilder(prefix);
        java.util.Random random = new java.util.Random();
        for (int i = 0; i < 32; i++) {
            sb.append(characters.charAt(random.nextInt(characters.length())));
        }
        String orderId = sb.toString();
        log.info("生成无规律订单ID成功: {}", orderId);
        return orderId;
    }

    @Override
    public long generateSnowflakeId(String businessName) {
        // 使用雪花算法生成分布式ID
        CosIdGenerator generator = getCosIdGenerator(businessName);
        long id = generator.generateAsLong();
        log.info("生成雪花算法ID成功: {}, businessName={}", id, businessName);
        return id;
    }

    @Override
    public long generateSegmentId(String businessName) {
        // 使用号段算法生成分布式ID
        CosIdGenerator generator = getCosIdGenerator(businessName);
        long id = generator.generateAsLong();
        log.info("生成号段算法ID成功: {}, businessName={}", id, businessName);
        return id;
    }

    @Override
    public String generateDistributedStringId(String businessName) {
        // 生成分布式字符串ID
        StringCosIdGenerator generator = getStringCosIdGenerator(businessName);
        String id = generator.generateAsString();
        log.info("生成分布式字符串ID成功: {}, businessName={}", id, businessName);
        return id;
    }
}
