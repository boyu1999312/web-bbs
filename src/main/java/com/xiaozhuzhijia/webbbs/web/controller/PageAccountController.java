package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.common.constant.LoginFinal;
import com.xiaozhuzhijia.webbbs.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/xzzj/bbs/account")
public class PageAccountController {

    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private UserService userService;

    @RequestMapping("/changePassword")
    public String changePassword(String head, String left, Model model){

        return userService.checkForgetPassword(head, left, model);
    }
}
