package com.xiaozhuzhijia.webbbs.common.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

public class CookieUtil {

    private CookieUtil(){}

    /**
     * 根据名称获取cookie
     * @param name
     * @param request
     * @return
     */
    public static Cookie getCookie(String name, HttpServletRequest request){
        if(StringUtils.isEmpty(name)){
            return null;
        }
        if(request == null){
            return null;
        }
        Cookie[] cookies = request.getCookies();
        if(cookies == null || cookies.length == 0){
            return null;
        }
        for (int i = 0; i < cookies.length; i++) {
            if(name.equals(cookies[i].getName())){
                return cookies[i];
            }
        }
        return null;
    }

}
