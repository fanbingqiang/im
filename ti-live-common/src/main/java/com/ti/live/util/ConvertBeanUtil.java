package com.ti.live.util;

import org.springframework.beans.BeanUtils;

public class ConvertBeanUtil {
    public static <T> T convert(Object source, Class<T> target) {
        if(source == null){
            return null;
        }
        T targetObj = null;
        try {
            targetObj = target.newInstance();
            BeanUtils.copyProperties(source, targetObj);
        }catch (Exception e) {
            e.printStackTrace();
        }
        return targetObj;
    }
}
