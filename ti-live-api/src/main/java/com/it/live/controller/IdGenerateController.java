package com.it.live.controller;

import com.it.live.entity.IdGenerateParam;
import com.it.live.entity.WebResDTO;
import com.ti.id.dto.*;
import com.ti.id.service.IIdGenerateService;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/id")
public class IdGenerateController {

    @DubboReference
    private IIdGenerateService idGenerateService;

    @PostMapping("/generate")
    public WebResDTO generateId(@RequestBody IdGenerateParam param) {
        try {
            IdGenerateRequest request = new IdGenerateRequest();
            request.setBusinessName(param.getBusinessName());
            request.setStringFormat(param.isStringFormat());

            IdGenerateResponse response = idGenerateService.generateId(request);

            return WebResDTO.success(response);
        } catch (Exception e) {
            return WebResDTO.error("ID生成失败: " + e.getMessage());
        }
    }

    @PostMapping("/batch")
    public WebResDTO batchGenerateId(@RequestBody IdGenerateParam param) {
        try {
            BatchIdGenerateRequest request = new BatchIdGenerateRequest();
            request.setBusinessName(param.getBusinessName());
            request.setCount(param.getCount() > 0 ? param.getCount() : 10);
            request.setStringFormat(param.isStringFormat());

            BatchIdGenerateResponse response = idGenerateService.batchGenerateId(request);

            return WebResDTO.success(response);
        } catch (Exception e) {
            return WebResDTO.error("批量ID生成失败: " + e.getMessage());
        }
    }

    @GetMapping("/generate/{businessName}")
    public WebResDTO generateIdSimple(@PathVariable String businessName) {
        try {
            String idStr = idGenerateService.generateIdAsString(businessName);
            return WebResDTO.success(idStr);
        } catch (Exception e) {
            return WebResDTO.error("ID生成失败: " + e.getMessage());
        }
    }

    @GetMapping("/generate/long/{businessName}")
    public WebResDTO generateIdLong(@PathVariable String businessName) {
        try {
            long id = idGenerateService.generateIdAsLong(businessName);
            return WebResDTO.success(id);
        } catch (Exception e) {
            return WebResDTO.error("ID生成失败: " + e.getMessage());
        }
    }
}
