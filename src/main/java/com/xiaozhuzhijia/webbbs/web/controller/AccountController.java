package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.common.constant.LoginFinal;
import com.xiaozhuzhijia.webbbs.common.dto.AuthDto;
import com.xiaozhuzhijia.webbbs.common.entity.UserBean;
import com.xiaozhuzhijia.webbbs.common.util.CookieUtil;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.web.service.UserService;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

@RestController
@RequestMapping("/xzzj/bbs/account")
public class AccountController {

    @Autowired
    private UserService userService;

    private Log log = LogFactory.getLog(AccountController.class);

    @PostMapping("/login")
    public Result login(AuthDto authDto, HttpServletResponse resp) {

        log.info("登录信息：" + authDto);

        Result result = userService.login(authDto);

        if (result.getCode() == 200) {
            Cookie cookie = (Cookie) result.getData();
            resp.addCookie(cookie);
            result.setData(null);
        }

        return result;
    }

    @PostMapping("/register")
    public Result register(AuthDto authDto) {

        log.info("得到的表单信息：" + authDto);

        return userService.register(authDto);
    }

    @GetMapping("/getCode")
    public Result getCode(AuthDto authDto) {

        log.info("获取验证码：" + authDto);

        return userService.getCode(authDto);
    }

    @PostMapping("/checkUserName")
    public Result checkUserName(String userName) {

        return userService.checkUserName(userName);
    }

    @PostMapping("/checkCode")
    public Result checkCode(AuthDto authDto) {

        log.info("检查验证码：" + authDto);
        return userService.checkCode(authDto);
    }

    @PostMapping("/checkEmail")
    public Result checkEmail(String email) {

        return userService.checkEmail(email);
    }

    @PostMapping("/emailExists")
    public Result emailExists(String email) {

        return userService.emailExists(email);
    }

    @PostMapping("/forgetPassword")
    public Result forgetPassword(AuthDto authDto, HttpServletResponse resp) {

        return userService.forgetPassword(authDto);
    }

    @PostMapping("/updPassword")
    public Result updPassword(AuthDto authDto) {

        log.info("修改密码的信息：" + authDto);
        return userService.updPassword(authDto);
    }
    @PostMapping("/getUserInfo")
    public Result getUserInfo() {

        return userService.getUserInfo();
    }
    @PostMapping("/logout")
    public Result logout(HttpServletResponse response){

        Cookie cookie = new Cookie(LoginFinal.COOKIE_LOGIN_TOKEN, "");
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);

        return userService.logout();
    }
    @PostMapping("/getUserByUserName")
    public Result getUserByUserName(String userName){

        return userService.getUserByUserName(userName);
    }
}
