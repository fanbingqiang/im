package com.it.live.controller;

import com.it.live.entity.MobileLoginParam;
import com.it.live.entity.WebResDTO;
import com.ti.IUserMobileRPCService;
import com.ti.IUserRPCService;
import com.ti.user.dto.UserDTO;
import com.ti.user.dto.MsgCheckDTO;
import com.ti.user.dto.userLoginDTO;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.dubbo.common.logger.Logger;
import org.apache.dubbo.common.logger.LoggerFactory;
import org.apache.dubbo.config.annotation.DubboReference;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class UserController {
    private Logger logger = LoggerFactory.getLogger(UserController.class);

    @DubboReference
    private IUserRPCService userRPCService;

    @DubboReference
    private IUserMobileRPCService userMobileRPCService;

    @RequestMapping("/getUserById")
    public WebResDTO getUserById(@RequestHeader(value = "X-User-Id", required = false) String userIdHeader) {
        if (userIdHeader == null || userIdHeader.isEmpty()) {
            return new WebResDTO(WebResDTO.FAIL, "X-User-Id header is missing");
        }
        Long userId = Long.parseLong(userIdHeader);
        logger.info("getUserById: " + userId);
        UserDTO userDTO = userRPCService.getUserById(userId);
        if (userDTO == null) {
            return new WebResDTO(WebResDTO.FAIL, "noexit");
        }
        return new WebResDTO(WebResDTO.SUCCESS, userDTO);
    }

    @PostMapping("/sendSMSCode")
    public WebResDTO sendSMSCode(@RequestBody String phone) {
        if (phone == null || phone.isEmpty()) {
            return new WebResDTO(WebResDTO.FAIL, "phone is null");
        }
        if (userRPCService.sendLoginCode(phone)) {
            return new WebResDTO(WebResDTO.SUCCESS, "success");
        } else {
            return new WebResDTO(WebResDTO.FAIL, "fail");
        }
    }

    @PostMapping("/mobileLogin")
    public WebResDTO mobileLogin(@RequestBody MobileLoginParam params, HttpServletResponse response) {
        if (params == null || params.getMobile() == null || params.getMobile().isEmpty()) {
            return new WebResDTO(WebResDTO.FAIL, "phone is null");
        }

        int code = params.getCode();
        String mobile = params.getMobile();

        if (code < 10000 || code > 99999) {
            return new WebResDTO(WebResDTO.FAIL, "code is error");
        }

        MsgCheckDTO msgCheckDTO = userRPCService.checkLoginCode(mobile, code);

        if (!msgCheckDTO.isCheckStatus()) {
            return new WebResDTO(WebResDTO.FAIL, msgCheckDTO.getMessage());
        }

        userLoginDTO loginResult = userMobileRPCService.login(mobile);

        if (!loginResult.isLoginSuccess()) {
            return new WebResDTO(WebResDTO.FAIL, loginResult.getDesc());
        }

        String token = userRPCService.createAndSaveLoginToken(loginResult.getUserId());
        Cookie cookie = new Cookie("tltk", token);
        cookie.setMaxAge(60 * 60 * 24 * 7);
        cookie.setPath("/");
        response.addCookie(cookie);

        return new WebResDTO(WebResDTO.SUCCESS, loginResult);
    }
}
