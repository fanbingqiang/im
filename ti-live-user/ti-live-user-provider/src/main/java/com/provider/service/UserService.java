package com.provider.service;

import com.provider.entity.SmsDO;
import com.provider.entity.User;
import com.provider.entity.UserDO;
import com.provider.mapper.SmsMapper;
import com.provider.mapper.UserMapper;
import com.ti.user.dto.UserDTO;
import com.ti.live.util.ConvertBeanUtil;
import jakarta.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Resource
    private UserMapper userMapper;

    @Resource
    private SmsMapper smsMapper;

    public UserDTO getUserById(Long id) {
        if (id == null || id <= 0) {
            return null;
        }
        UserDO userDO = userMapper.selectById(id);
        if (userDO != null) {
            UserDTO userDTO = ConvertBeanUtil.convert(userDO, UserDTO.class);
            return userDTO;
        }
        return null;
    }

    public boolean sendLoginCode(String phone, int code) {
        SmsDO smsDO = new SmsDO();
        smsDO.setPhone(phone);
        smsDO.setCode(code);
        smsDO.setSceneType(1);
        smsDO.setStatus(0);
        int rows = smsMapper.insert(smsDO);
        return rows > 0;
    }

    public boolean checkLoginCode(String phone, int code) {
        User user = userMapper.selectOne(
            new com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper<User>()
                .eq(User::getPhone, phone)
        );
        return user != null;
    }
}

