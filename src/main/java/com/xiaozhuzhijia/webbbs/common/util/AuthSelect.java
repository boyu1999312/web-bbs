package com.xiaozhuzhijia.webbbs.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.constant.XZZJFinal;
import com.xiaozhuzhijia.webbbs.common.entity.UserBean;
import com.xiaozhuzhijia.webbbs.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.annotation.PostConstruct;
import java.util.Objects;

@Component
public class AuthSelect {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redis;

    private static AuthSelect select;
    private AuthSelect(){}

    @PostConstruct
    private void get(){
        select = this;
    }

    public static UserBean getUser(Integer id){
        if(IntegerUtils.isEmpty(id)){
            return null;
        }
        UserBean userBean;
        String userInfo = select.redis.opsForValue().get(XZZJFinal.USER_PREFIX + id);
        if(StringUtils.isEmpty(userInfo)){
             userBean = select.userMapper.selectOne(new QueryWrapper<UserBean>()
                    .eq("id", id));
            if(Objects.isNull(userBean)){
                return null;
            }
            String json = JsonMapper.toJson(userBean);
            select.redis.opsForValue().set(XZZJFinal.USER_PREFIX + id, json);
            return userBean;
        }
        userBean = JsonMapper.toObject(userInfo, UserBean.class);
        return userBean;

    }


}
