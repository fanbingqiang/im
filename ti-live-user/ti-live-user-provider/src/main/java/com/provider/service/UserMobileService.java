package com.provider.service;

import org.springframework.stereotype.Service;
import com.ti.user.dto.userLoginDTO;

@Service
public class UserMobileService {
    public userLoginDTO login(String mobile) {
        if(mobile == null){
            return null;
        }
        return null;
    }
}
