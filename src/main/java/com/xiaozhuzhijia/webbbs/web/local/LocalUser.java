package com.xiaozhuzhijia.webbbs.web.local;

import com.xiaozhuzhijia.webbbs.common.vo.UserVo;

public class LocalUser {

    private static final ThreadLocal<UserVo> local = new ThreadLocal<>();

    public static void put(UserVo userVo){

        local.set(userVo);
    }

    public static UserVo get(){

        return local.get();
    }

    public static void remove(){

        local.remove();
    }

}
