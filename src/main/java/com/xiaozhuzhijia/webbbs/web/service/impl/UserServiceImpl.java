package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.xiaozhuzhijia.webbbs.common.dto.AuthDto;
import com.xiaozhuzhijia.webbbs.common.entity.UserBean;
import com.xiaozhuzhijia.webbbs.common.util.MailUtil;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.web.mapper.UserMapper;
import com.xiaozhuzhijia.webbbs.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redis;
    /**
     * 注册
     * @param authDto
     * @return
     */
    @Override
    public Result register(AuthDto authDto) {

        //将DTO数据封装到PO对象中
        UserBean userBean = new UserBean().setUserName(authDto.getAcc())
                .setEmail(authDto.getEmail())
                .setGender(authDto.getGender())
                .setCreatedTime(authDto.getCreatedTime())
                .setUpdatedTime(authDto.getUpDatedTime());

        //将密码加密成MD5值
        String pwdMd5 = DigestUtils.md5DigestAsHex(
                (authDto.getPwd() + System.currentTimeMillis()).getBytes());
        userBean.setPassWord(pwdMd5);

        //检验验证码
        String code = redis.opsForValue().get(authDto.getCodeCache());
        if(!code.equals(authDto.getCode().toUpperCase())){
            return Result.error("邮箱验证码错误");
        }

        try {
            int index = userMapper.insert(userBean);
            //注册成功
            if(1 == index) {
                return Result.ok();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("注册|数据库写入错误：" + e);
        }
        return Result.ok("注册成功");
    }

    /**
     * 获取邮箱验证码
     * @param authDto
     */
    @Override
    public Result getCode(AuthDto authDto) {

        String code = "";
        for (int i = 0; i < 5; i++) {
            code += ((char)('A' + (Math.random()*26)));
        }

        try {
            //发送邮箱认证邮件
            MailUtil.send(authDto.getEmail(), code);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("注册|发送邮箱认证错误：" + e);
        }

        try {
            //将验证码存入redis，120s
            redis.opsForValue().set(authDto.getCodeCache(), code, 120, TimeUnit.MINUTES);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("注册|存入验证码错误：" + e);
        }
        return Result.ok();
    }
}
