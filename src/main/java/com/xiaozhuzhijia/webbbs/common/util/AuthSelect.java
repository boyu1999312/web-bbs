package com.xiaozhuzhijia.webbbs.common.util;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.constant.XZZJFinal;
import com.xiaozhuzhijia.webbbs.common.entity.UserBean;
import com.xiaozhuzhijia.webbbs.web.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Objects;

@Component
public class AuthSelect {

    @Autowired
    private static UserMapper userMapper;
    @Autowired
    private static StringRedisTemplate redis;


    public static UserBean getUser(Integer id){
        if(IntegerUtils.isEmpty(id)){
            return null;
        }
        UserBean userBean;
        String userInfo = redis.opsForValue().get(XZZJFinal.USER_PREFIX + id);
        if(StringUtils.isEmpty(userInfo)){
             userBean = userMapper.selectOne(new QueryWrapper<UserBean>()
                    .eq("user_name", id));
            if(Objects.isNull(userBean)){
                return null;
            }
            String json = JsonMapper.toJson(userBean);
            redis.opsForValue().set(XZZJFinal.USER_PREFIX + id, json);
            return userBean;
        }
        userBean = JsonMapper.toObject(userInfo, UserBean.class);
        return userBean;

    }

}
