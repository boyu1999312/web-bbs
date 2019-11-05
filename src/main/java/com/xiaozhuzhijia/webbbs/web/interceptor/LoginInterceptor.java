package com.xiaozhuzhijia.webbbs.web.interceptor;


import com.xiaozhuzhijia.webbbs.common.constant.LoginFinal;
import com.xiaozhuzhijia.webbbs.common.util.CookieUtil;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redis;

    private Log log = LogFactory.getLog(LoginInterceptor.class);

    private Set<String> currentUrl = new HashSet<>();

    private boolean checkCurrentUrl(String url){
        currentUrl.add("/xzzj/login");
        currentUrl.add("/xzzj/bbs/account");
        return true;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        log.info("当前访问网址：" + servletPath);
        if(checkCurrentUrl(servletPath)){
            return true;
        }
        Cookie cookie = CookieUtil.getCookie(LoginFinal.COOKIE_LOGIN_TOKEN, request);
        if(Objects.isNull(cookie)){
            response.sendRedirect("/xzzj/login");
            return false;
        }
        try {
            String token = redis.opsForValue().get(LoginFinal.COOKIE_LOGIN_TOKEN);
            if(StringUtils.isEmpty(token)){
                response.sendRedirect("/xzzj/login");
                return false;
            }else{
                if(cookie.getValue().equals(token)){
                    return true;
                }else {
                    response.sendRedirect("/xzzj/login");
                    return false;
                }
            }
        } catch (Exception e){
            e.printStackTrace();
            response.sendRedirect("/xzzj/login");
            return false;
        }
    }



    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
