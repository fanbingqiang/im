package com.provider.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.provider.entity.SmsDO;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface SmsMapper extends BaseMapper<SmsDO> {
}
