package com.ti;

import com.ti.user.dto.UserDTO;
import com.ti.user.dto.MsgCheckDTO;

public interface IUserRPCService {
    UserDTO getUserById(Long userId);

    boolean sendLoginCode(String phone);

    MsgCheckDTO checkLoginCode(String mobile, int code);

    String createAndSaveLoginToken(Long userId);

    Long getUserIdByToken(String token);
}
