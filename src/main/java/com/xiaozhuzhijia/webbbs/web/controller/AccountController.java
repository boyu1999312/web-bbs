package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.common.dto.AuthDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.web.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xzzj/bbs/account")
public class AccountController {

    @Autowired
    private UserService userService;

    private Log log = LogFactory.getLog(AccountController.class);

    @PostMapping("/register")
    private Result register(AuthDto authDto){

        log.info("得到的表单信息：" + authDto);

        return userService.register(authDto);
    }

    @GetMapping("/getCode")
    private Result getCode(AuthDto authDto){

        log.info("获取验证码：" + authDto);

        return userService.getCode(authDto);
    }
}
