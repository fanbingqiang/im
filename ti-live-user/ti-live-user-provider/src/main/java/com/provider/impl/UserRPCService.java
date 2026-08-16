package com.provider.impl;

import com.provider.entity.UserDO;
import com.provider.mapper.UserMapper;
import com.provider.service.SmsService;
import com.provider.service.TokenService;
import com.ti.IUserRPCService;
import com.ti.user.dto.UserDTO;
import com.ti.live.util.ConvertBeanUtil;
import com.ti.user.dto.MsgCheckDTO;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;
import org.springframework.data.redis.core.RedisTemplate;

@DubboService
public class UserRPCService implements IUserRPCService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private SmsService smsService;

    @Resource
    private TokenService tokenService;

    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public UserDTO getUserById(Long userId) {
        UserDO userDO = userMapper.selectById(userId);
        return ConvertBeanUtil.convert(userDO, UserDTO.class);
    }

    @Override
    public boolean sendLoginCode(String phone) {
        return smsService.sendLoginCode(phone);
    }

    @Override
    public MsgCheckDTO checkLoginCode(String mobile, int code) {
        return smsService.checkLoginCode(mobile, code);
    }

    @Override
    public String createAndSaveLoginToken(Long userId) {
        return tokenService.createAndSaveLoginToken(userId);
    }

    @Override
    public Long getUserIdByToken(String token) {
        return tokenService.getUserIdByToken(token);
    }
}
