package com.xiaozhuzhijia.webbbs.web.interceptor;


import com.xiaozhuzhijia.webbbs.common.constant.LoginFinal;
import com.xiaozhuzhijia.webbbs.common.entity.UserBean;
import com.xiaozhuzhijia.webbbs.common.util.CookieUtil;
import com.xiaozhuzhijia.webbbs.common.util.JsonMapper;
import com.xiaozhuzhijia.webbbs.common.vo.UserVo;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.*;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private StringRedisTemplate redis;

    private Log log = LogFactory.getLog(LoginInterceptor.class);

    private Map<String, String> currentUrl = new HashMap<>();

    private final String ANON = "anon";

    /**
     * 将url过滤
     * @param url
     * @return
     */
    private boolean checkCurrentUrl(String url){
        currentUrl.put("/xzzj/login", ANON);
        currentUrl.put("/xzzj/bbs/account/", ANON);
        currentUrl.put("/static/", ANON);
        for (String key : currentUrl.keySet()) {
            if(url.contains(key)){
                return ANON.equals(currentUrl.get(key));
            }
        }
        return false;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String servletPath = request.getServletPath();
        log.info("当前访问网址：" + servletPath);

        Cookie cookie = CookieUtil.getCookie(LoginFinal.COOKIE_LOGIN_TOKEN, request);
        if(Objects.isNull(cookie)){
            if(checkCurrentUrl(servletPath)){
                return true;
            }
            response.sendRedirect("/xzzj/login");
            return false;
        }
        try {
            String userInfo = redis.opsForValue().get(cookie.getValue());
            if(StringUtils.isEmpty(userInfo)){
                if(checkCurrentUrl(servletPath)){
                    return true;
                }
                response.sendRedirect("/xzzj/login");
                return false;
            }else{
                UserVo userVo = JsonMapper.toObject(userInfo, UserVo.class);
                LocalUser.put(userVo);
                return true;
            }

        } catch (Exception e){
            e.printStackTrace();
            response.sendRedirect("/xzzj/login");
            return false;
        }
    }


    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

        LocalUser.remove();
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
