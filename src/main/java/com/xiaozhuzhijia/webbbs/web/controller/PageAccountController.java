package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageAccountController {

    @Autowired
    private StringRedisTemplate redis;
    @Autowired
    private UserService userService;

    @RequestMapping("/xzzj/bbs/account/changePassword")
    public String changePassword(String head, String left, Model model){

        return userService.checkForgetPassword(head, left, model);
    }
    @RequestMapping("/")
    public String index(){

        return "xzzj_home";
    }
}
