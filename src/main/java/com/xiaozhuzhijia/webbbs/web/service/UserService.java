package com.xiaozhuzhijia.webbbs.web.service;

import com.xiaozhuzhijia.webbbs.common.dto.AuthDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import org.springframework.ui.Model;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

public interface UserService {


    Result register(AuthDto authDto, HttpServletRequest request);

    Result getCode(AuthDto authDto, HttpServletRequest request);

    Result checkUserName(String userName);

    Result checkCode(AuthDto authDto);

    Result checkEmail(String email);

    Result login(AuthDto authDto);

    Result emailExists(String email);

    Result forgetPassword(AuthDto authDto);

    String checkForgetPassword(String head, String left, Model model);

    Result updPassword(AuthDto authDto);

    Result getUserInfo();

    Result logout();

    Result getUserByUserName(String userName);

    Result setPic(MultipartFile file);

}
