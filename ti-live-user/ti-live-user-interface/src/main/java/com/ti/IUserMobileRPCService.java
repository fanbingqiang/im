package com.ti;

import com.ti.user.dto.userLoginDTO;

public interface IUserMobileRPCService {
    userLoginDTO login(String mobile);
}
