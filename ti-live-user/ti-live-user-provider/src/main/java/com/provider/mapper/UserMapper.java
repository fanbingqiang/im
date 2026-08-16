package com.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.provider.entity.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper extends BaseMapper<User> {
}
