package com.ti.id.service;

import com.ti.id.dto.*;

public interface IIdGenerateService {
    
    /**
     * 生成单个ID
     * @param request 请求参数
     * @return 生成结果
     */
    IdGenerateResponse generateId(IdGenerateRequest request);

    /**
     * 批量生成ID
     * @param request 批量请求参数
     * @return 批量生成结果
     */
    BatchIdGenerateResponse batchGenerateId(BatchIdGenerateRequest request);

    /**
     * 生成序列化格式ID（String）
     * @param businessName 业务名称
     * @return 字符串ID
     */
    String generateIdAsString(String businessName);

    /**
     * 生成非序列化格式ID（long）
     * @param businessName 业务名称
     * @return 长整型ID
     */
    long generateIdAsLong(String businessName);

    /**
     * 生成序列化订单ID
     * @param prefix 订单前缀
     * @return 序列化订单ID
     */
    String generateSequentialOrderId(String prefix);

    /**
     * 生成无规律订单ID
     * @param prefix 订单前缀
     * @return 无规律订单ID
     */
    String generateRandomOrderId(String prefix);

    /**
     * 生成分布式雪花算法ID
     * @param businessName 业务名称
     * @return 雪花算法ID
     */
    long generateSnowflakeId(String businessName);

    /**
     * 生成分布式号段算法ID
     * @param businessName 业务名称
     * @return 号段算法ID
     */
    long generateSegmentId(String businessName);

    /**
     * 生成分布式字符串ID
     * @param businessName 业务名称
     * @return 字符串ID
     */
    String generateDistributedStringId(String businessName);
}
