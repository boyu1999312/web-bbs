package com.xiaozhuzhijia.webbbs.web.service;

import com.xiaozhuzhijia.webbbs.common.dto.AuthDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;

public interface UserService {


    Result register(AuthDto authDto);

    Result getCode(AuthDto authDto);

    Result checkUserName(String userName);

    Result checkCode(AuthDto authDto);

    Result checkEmail(String email);

    Result login(AuthDto authDto);

    Result emailExists(String email);

}
