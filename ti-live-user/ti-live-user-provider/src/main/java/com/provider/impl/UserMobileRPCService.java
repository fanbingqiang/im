package com.provider.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.provider.entity.User;
import com.provider.mapper.UserMapper;
import com.provider.service.TokenService;
import com.ti.IUserMobileRPCService;
import com.ti.user.dto.userLoginDTO;
import jakarta.annotation.Resource;
import org.apache.dubbo.config.annotation.DubboService;

@DubboService
public class UserMobileRPCService implements IUserMobileRPCService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private TokenService tokenService;

    @Override
    public userLoginDTO login(String mobile) {
        userLoginDTO result = new userLoginDTO();

        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhone, mobile);
        User user = userMapper.selectOne(queryWrapper);

        if (user == null) {
            user = new User();
            user.setUsername("user_" + mobile);
            user.setPhone(mobile);
            user.setNickname("用户" + mobile.substring(mobile.length() - 4));
            user.setStatus(1);
            user.setUserType(1);
            userMapper.insert(user);
        }

        String token = tokenService.createAndSaveLoginToken(user.getId());

        result.setLoginSuccess(true);
        result.setUserId(user.getId());
        result.setDesc("登录成功");

        return result;
    }
}
