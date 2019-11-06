package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.constant.LoginFinal;
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
import org.springframework.util.StringUtils;

import javax.servlet.http.Cookie;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redis;

    /**
     * 登录
     * @param authDto
     * @return
     */
    @Override
    public Result login(AuthDto authDto) {

        //查询用户名或邮箱
        UserBean userBean = userMapper.selectOne(new QueryWrapper<UserBean>(
                ).eq("user_name", authDto.getAccEmail())
                .select("user_name", "email", "salt", "pass_word")
                .or().eq("email", authDto.getAccEmail()));

        if(Objects.isNull(userBean)){
            return Result.error("用户名或邮箱不存在");
        }

        System.out.println(userBean);
        String pwdMd5 = DigestUtils.md5DigestAsHex(
                (authDto.getLPwd() + userBean.getSalt()).getBytes());
        if(!pwdMd5.equals(userBean.getPassWord())){
            return Result.error("密码错误");
        }

        //用账户和毫秒值生成token
        String loginToken = DigestUtils.md5DigestAsHex((authDto.getAccEmail() + "XZZJ_LOGIN"
                + System.currentTimeMillis()).getBytes());
        int time = 3600 * 24 * 30 * 6;
        Cookie cookie = new Cookie(LoginFinal.COOKIE_LOGIN_TOKEN, loginToken);
        cookie.setMaxAge(time);//设置半年
        cookie.setPath("/");

        try {
            redis.opsForValue().set(LoginFinal.COOKIE_LOGIN_TOKEN, loginToken, time, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("登录系统失效，请联系网站管理员");
        }

        return Result.ok(cookie);
    }

    /**
     * 检查邮箱是否为本站已注册邮箱
     * @param email
     * @return
     */
    @Override
    public Result emailExists(String email) {

        UserBean userBean = userMapper.selectOne(new QueryWrapper<UserBean>()
                .eq("email", email));
        if(Objects.isNull(userBean)){
            return Result.error("该邮箱未注册");
        }
        return Result.ok("邮箱正确");
    }


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
                .setUpdatedTime(authDto.getCreatedTime())
                .setSalt(System.currentTimeMillis() + "");

        try {
            //从redis中获取注册信息
            //进行信息比对，如果不匹配，提示注册信息有误
            String auth = redis.opsForValue().get("auth," + authDto.getCodeCache());
            if(StringUtils.isEmpty(auth)){
                return Result.error("注册超时，请重新注册");
            }
            String[] auths = auth.split(",");
            if(!auths[0].equals(authDto.getEmail())){
                return Result.error("注册信息不匹配");
            }
        } catch (Exception e){
            e.printStackTrace();
            return Result.error("注册系统错误，请联系网站管理员");
        }

        //将密码加密成MD5值
        String pwdMd5 = DigestUtils.md5DigestAsHex(
                (authDto.getPwd() + userBean.getSalt()).getBytes());
        userBean.setPassWord(pwdMd5);

        try {
            int index = userMapper.insert(userBean);
            //注册成功
            if(1 == index) {
                return Result.ok();
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("数据库写入错误");
        }
        return Result.ok("注册成功");
    }

    /**
     * 用户名查重
     * @param userName
     * @return
     */
    @Override
    public Result checkUserName(String userName) {

        UserBean userBean = userMapper.selectOne(new QueryWrapper<UserBean>(
                new UserBean().setUserName(userName)).select("user_name"));
        return Objects.isNull(userBean)
                ? Result.ok("用户名可用")
                : Result.error("用户名已注册");
    }

    /**
     * 邮箱查重
     * @param email
     * @return
     */
    @Override
    public Result checkEmail(String email) {

        UserBean userBean = userMapper.selectOne(new QueryWrapper<UserBean>(
                new UserBean().setEmail(email)).select("email"));
        return Objects.isNull(userBean)
                ? Result.ok("邮箱可用")
                : Result.error("邮箱已注册");
    }


    /**
     * 检验验证码
     * @param authDto
     * @return
     */
    @Override
    public Result checkCode(AuthDto authDto) {

        //检验验证码
        try {
            //获取redis中的验证信息
            String emailCode = redis.opsForValue().get(authDto.getCodeCache());
            if(Objects.isNull(emailCode)){
                return Result.error("邮箱验证码未发送");
            }
            String[] emailCodes = emailCode.split(",");
            if(!authDto.getEmail().equals(emailCodes[0])){
                return Result.error("请确认邮箱是否正确");
            }
            if(!emailCodes[1].equals(authDto.getCode().toUpperCase())){
                return Result.error("邮箱验证码错误");
            }
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("注册系统错误，请联系网站管理员");
        }
        return Result.ok("验证码正确");
    }



    /**
     * 获取邮箱验证码
     * @param authDto
     */
    @Override
    public Result getCode(AuthDto authDto) {

        String emailCode = authDto.getEmail() + ",";
        for (int i = 0; i < 5; i++) {
            emailCode += ((char)('A' + (Math.random()*26)));
        }

        try {
            //发送邮箱认证邮件
            MailUtil.send(authDto.getEmail(), emailCode);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("网络原因，请等待10分钟后重试");
        }

        try {
            //将验证码存入redis，120s
            redis.opsForValue().set(authDto.getCodeCache(), emailCode,
                    120, TimeUnit.SECONDS);

            //将注册信息存入redis,30m
            redis.opsForValue().set(
                    "auth," + authDto.getCodeCache(),
                    emailCode, 30, TimeUnit.MINUTES);

        }catch (Exception e){
            e.printStackTrace();
            return Result.error("注册系统错误，请联系网站管理员");
        }
        return Result.ok();
    }


}
