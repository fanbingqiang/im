package com.it.live.controller;

import com.it.live.entity.ImRegisterParam;
import com.it.live.entity.WebResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/im/service")
public class ImServiceController {

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    private static final String IM_SERVICE_KEY = "im:service:registry";

    @PostMapping("/register")
    public WebResDTO registerImService(@RequestBody ImRegisterParam param) {
        try {
            if (param.getImServiceName() == null || param.getWsUrl() == null) {
                return WebResDTO.fail("参数不完整");
            }

            Map<String, String> serviceInfo = new HashMap<>();
            serviceInfo.put("imServiceName", param.getImServiceName());
            serviceInfo.put("wsUrl", param.getWsUrl());
            serviceInfo.put("wsPort", String.valueOf(param.getWsPort()));
            serviceInfo.put("registerTime", String.valueOf(System.currentTimeMillis()));
            serviceInfo.put("status", "online");

            stringRedisTemplate.opsForHash().putAll(
                    IM_SERVICE_KEY + ":" + param.getImServiceName(), serviceInfo
            );

            stringRedisTemplate.opsForSet().add(IM_SERVICE_KEY, param.getImServiceName());

            return WebResDTO.success("IM服务注册成功");
        } catch (Exception e) {
            return WebResDTO.fail("IM服务注册失败: " + e.getMessage());
        }
    }

    @PostMapping("/unregister")
    public WebResDTO unregisterImService(@RequestParam String imServiceName) {
        try {
            stringRedisTemplate.opsForSet().remove(IM_SERVICE_KEY, imServiceName);
            stringRedisTemplate.delete(IM_SERVICE_KEY + ":" + imServiceName);

            return WebResDTO.success("IM服务已注销");
        } catch (Exception e) {
            return WebResDTO.fail("IM服务注销失败: " + e.getMessage());
        }
    }

    @GetMapping("/list")
    public WebResDTO getImServiceList() {
        try {
            Set<String> serviceNames = stringRedisTemplate.opsForSet().members(IM_SERVICE_KEY);
            List<Map<String, String>> services = new ArrayList<>();

            for (String name : serviceNames) {
                Map<Object, Object> entries = stringRedisTemplate.opsForHash()
                        .entries(IM_SERVICE_KEY + ":" + name);
                Map<String, String> service = new HashMap<>();
                entries.forEach((k, v) -> service.put(k.toString(), v.toString()));
                services.add(service);
            }

            return WebResDTO.success(services);
        } catch (Exception e) {
            return WebResDTO.fail("获取IM服务列表失败: " + e.getMessage());
        }
    }

    @GetMapping("/wsUrl")
    public WebResDTO getWsUrl(@RequestParam String imServiceName) {
        try {
            String wsUrl = (String) stringRedisTemplate.opsForHash()
                    .get(IM_SERVICE_KEY + ":" + imServiceName, "wsUrl");
            String wsPort = (String) stringRedisTemplate.opsForHash()
                    .get(IM_SERVICE_KEY + ":" + imServiceName, "wsPort");

            if (wsUrl == null) {
                return WebResDTO.fail("IM服务不存在");
            }

            Map<String, String> result = new HashMap<>();
            result.put("wsUrl", wsUrl);
            result.put("wsPort", wsPort);

            return WebResDTO.success(result);
        } catch (Exception e) {
            return WebResDTO.fail("获取WebSocket地址失败: " + e.getMessage());
        }
    }

    @PostMapping("/heartbeat")
    public WebResDTO heartbeat(@RequestParam String imServiceName) {
        try {
            stringRedisTemplate.opsForHash()
                    .put(IM_SERVICE_KEY + ":" + imServiceName, "lastHeartbeat",
                            String.valueOf(System.currentTimeMillis()));
            return WebResDTO.success("心跳成功");
        } catch (Exception e) {
            return WebResDTO.fail("心跳更新失败: " + e.getMessage());
        }
    }
}
