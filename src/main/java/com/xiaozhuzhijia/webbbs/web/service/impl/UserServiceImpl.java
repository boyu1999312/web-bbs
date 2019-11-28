package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.constant.XZZJFinal;
import com.xiaozhuzhijia.webbbs.common.dto.AuthDto;
import com.xiaozhuzhijia.webbbs.common.entity.UserBean;
import com.xiaozhuzhijia.webbbs.common.util.*;
import com.xiaozhuzhijia.webbbs.common.vo.UserVo;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import com.xiaozhuzhijia.webbbs.web.mapper.UserMapper;
import com.xiaozhuzhijia.webbbs.web.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.concurrent.TimeUnit;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private StringRedisTemplate redis;

    @Value("${xzzj.local}")
    private String local;
    @Value("${xzzj.imgPath}")
    private String path;

    /**
     * 登录
     * @param authDto
     * @return
     */
    @Override
    public Result login(AuthDto authDto) {
        //查询用户名或邮箱
        UserBean userBean = userMapper.selectOne(new QueryWrapper<UserBean>()
                .eq("user_name", authDto.getAccEmail())
                .select("id", "user_name", "email", "salt",
                        "pass_word", "nick_name", "portrait",
                        "created_time")
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


        return Result.ok(loginCookie(authDto.getAccEmail(), userBean));
    }

    /**
     * 根据用户信息创建登录cookie
     * @param accEmail
     * @param userBean
     * @return
     */
    private Cookie loginCookie(String accEmail, UserBean userBean){
        //用账户和毫秒值生成token
        String loginToken = DigestUtils.md5DigestAsHex((accEmail
                + "XZZJ_LOGIN" + System.currentTimeMillis()).getBytes());
        int time = 3600 * 24 * 30 * 6;
        Cookie cookie = new Cookie(XZZJFinal.COOKIE_LOGIN_TOKEN, loginToken);
        cookie.setMaxAge(time);//设置半年
        cookie.setPath("/");
        //初始化UserInfo
        UserVo userVo = new UserVo()
                .setId(userBean.getId())
                .setUserName(userBean.getUserName())
                .setNickName(userBean.getNickName())
                .setPortrait(StringUtils.isEmpty(userBean.getPortrait()) ?
                        "/images/pig_head.jpg" : userBean.getPortrait())
                .setToken(loginToken)
                .setRegisterTime(userBean.getCreatedTime())
                .setEmail(userBean.getEmail());
        String userInfo = JsonMapper.toJson(userVo);

        redis.opsForValue().set(loginToken,
                userInfo, time, TimeUnit.SECONDS);

        return cookie;
    }

    /**
     * 检查邮箱是否为本站已注册邮箱
     * @param email
     * @return
     */
    @Override
    public Result emailExists(String email) {

        UserBean userBean = userMapper.selectOne(new QueryWrapper<UserBean>()
                .select("email").eq("email", email));
        if(Objects.isNull(userBean)){
            return Result.error("该邮箱未注册");
        }
        return Result.okMsg("邮箱正确");
    }

    /**
     * 注册
     * @param authDto
     * @return
     */
    @Override
    public Result register(AuthDto authDto, HttpServletRequest request) {

        //将DTO数据封装到PO对象中
        Date date = new Date();
        UserBean userBean = new UserBean().setUserName(authDto.getAcc())
                .setEmail(authDto.getEmail())
                .setGender(authDto.getGender())
                .setCreatedTime(date)
                .setUpdatedTime(date)
                .setSalt(System.currentTimeMillis() + "");

        //从session中把token取出来校验
        String token = (String) request.getSession()
                .getAttribute(authDto.getEmail() + XZZJFinal.REGISTER_TOKEN);
        System.out.println("获取的token:" + token);
        System.out.println(authDto.getToken());
        if(!TokenUtil.equalsToken(token, authDto.getToken())){
            return Result.error("请勿重复提交");
        }

        //将密码加密成MD5值
        String pwdMd5 = DigestUtils.md5DigestAsHex(
                (authDto.getPwd() + userBean.getSalt()).getBytes());
        userBean.setPassWord(pwdMd5)
                .setNickName(authDto.getAcc());

        int index = userMapper.insert(userBean);
        //注册成功
        if(1 != index) {
            return Result.error("注册失败");
        }

        redis.delete(authDto.getCodeCache());
        request.getSession().removeAttribute(authDto.getEmail() + XZZJFinal.REGISTER_TOKEN);
        return Result.ok(loginCookie(authDto.getAcc(), userBean));
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
                ? Result.okMsg("用户名可用")
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
                ? Result.okMsg("邮箱可用")
                : Result.error("邮箱已注册");
    }

    private Result defaultCheckCode(AuthDto authDto){
        if(Objects.isNull(authDto)){
            return Result.error("信息有误，请刷新重试");
        }
        //检验验证码

        //获取redis中的验证信息
        String emailCode = redis.opsForValue().get(authDto.getCodeCache());
        if(Objects.isNull(emailCode)){
            return Result.error("邮箱验证码无效");
        }
        String[] emailCodes = emailCode.split(",");
        if(!authDto.getEmail().equals(emailCodes[0])){
            return Result.error("请确认邮箱是否正确");
        }
        if(!emailCodes[1].equals(authDto.getCode().toUpperCase())){
            return Result.error("邮箱验证码错误");
        }


        return Result.okMsg("验证码正确");
    }

    /**
     * 检验验证码
     * @param authDto
     * @return
     */
    @Override
    public Result checkCode(AuthDto authDto) {

        return defaultCheckCode(authDto);
    }



    /**
     * 获取邮箱验证码
     * @param authDto
     */
    @Override
    public Result getCode(AuthDto authDto, HttpServletRequest request) {

        String emailCode = authDto.getEmail() + ",";
        for (int i = 0; i < 5; i++) {
            emailCode += ((char)('A' + (Math.random()*26)));
        }

        try {
            //发送邮箱认证邮件
            MailUtil.send(authDto.getEmail(), authDto.getAcc(), emailCode);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("网络原因，请等待10分钟后重试");
        }


        //将验证码存入redis，300s
        redis.opsForValue().set(authDto.getCodeCache(), emailCode,
                300, TimeUnit.SECONDS);
        String toekn = TokenUtil.getToekn(authDto.getEmail());
        request.getSession().setAttribute(authDto.getEmail() +
                        XZZJFinal.REGISTER_TOKEN, toekn);
        System.out.println("生成token:" + toekn);
        return Result.ok(toekn);
    }


    /**
     * 返回修改密码页面
     * 有效时间3小时
     * @param authDto
     * @return
     */
    @Override
    public Result forgetPassword(AuthDto authDto) {

        Result result = defaultCheckCode(authDto);
        if(result.getCode() == 200){

            result.setMsg("已发送修改密码链接到您的邮箱，请注意查收");

            String emailMD5 = DigestUtils.md5DigestAsHex((authDto
                    .getEmail() + System.currentTimeMillis()).getBytes());
            String codeMD5 = DigestUtils.md5DigestAsHex((authDto
                    .getCode() + System.currentTimeMillis()).getBytes());
            String temp = emailMD5 + XZZJFinal.FORGET_PASSWORD_TOKEN + codeMD5;

            String text =   "小猪之家的忘记密码邮件：<br/>" +
                            "哦！糟糕！忘记密码了吗！呆胶布！点击下面这个链接就可以重新设置您的密码了呢！<br/>" +
                            "<a href='http://" + local + "/xzzj/bbs/account/changePassword?head=" +
                            emailMD5 + "&left=" + codeMD5 + "' >点此重置密码</a>  <br/>" +
                            "时间只有 3 个小时，要及时哦~！<br/>" +
                            "如果不是本人，请忽略此邮件！";


            //将key 为 邮箱+固定token 的信息存入redis 3 小时
            redis.opsForValue().set(temp, authDto.getEmail(),
                    60 * 3, TimeUnit.MINUTES);
            try {
                MailUtil.sendForgetPassword(authDto.getEmail(),"", text);
            } catch (Exception e){
                e.printStackTrace();
                return Result.error("邮箱发送失败，请十分钟后重试");
            }


        }
        return result;
    }

    /**
     * 根据 head 和 left
     * 检查这个用户是否正在修改密码
     * @param head
     * @param left
     * @return
     */
    @Override
    public String checkForgetPassword(String head, String left, Model model) {

        String email = redis.opsForValue().get(head +
                XZZJFinal.FORGET_PASSWORD_TOKEN + left);
        if(StringUtils.isEmpty(email)){
            return "error/404";
        }

        model.addAttribute("head", head);
        model.addAttribute("left", left);

        return "forget_password";
    }

    /**
     * 根据redi中的邮箱修改密码
     * @param authDto
     * @return
     */
    @Override
    public Result updPassword(AuthDto authDto) {
        String key = authDto.getHead()
                + XZZJFinal.FORGET_PASSWORD_TOKEN
                + authDto.getLeft();
        String email = "";

        email = redis.opsForValue().get(key);

        if(StringUtils.isEmpty(email)){
            return Result.error("超时，请重新修改密码");
        }

        UserBean userBean = userMapper.selectOne(
                new QueryWrapper<UserBean>()
                        .select("version")
                        .eq("email", email));

        String salt = System.currentTimeMillis() + "";

        userBean.setSalt(salt)
                .setUpdatedTime(authDto.getUpdatedTime())
                .setVersion(userBean.getVersion() + 1);

        String PwdMD5 = DigestUtils.md5DigestAsHex(
                (authDto.getPwd() + salt).getBytes());
        userBean.setPassWord(PwdMD5);

        int index = userMapper.update(userBean,
                new QueryWrapper<UserBean>()
                .eq("email", email));
        if(index == 0){
            return Result.error("系统错误，请刷新重试");
        }
        redis.delete(key);
        return Result.okMsg("修改密码成功，3 秒后自动跳转");
    }

    /**
     * 获取用户信息
     * @return
     */
    @Override
    public Result getUserInfo() {

        UserVo userVo = LocalUser.get();
        if(Objects.isNull(userVo)){
            return Result.error("无法找到用户");
        }
        userVo.setToken(null).setId(null);
        return Result.ok(userVo);
    }

    /**
     * 用户注销
     * @return
     */
    @Override
    public Result logout() {

        UserVo userVo = LocalUser.get();
        Boolean delete = redis.delete(userVo.getToken());
        if(Objects.isNull(userVo) || !delete){
            return Result.error("退出失败");
        }
        return Result.ok();
    }

    /**
     * 根据用户名查找用户
     * @param userName
     * @return
     */
    @Override
    public Result getUserByUserName(String userName) {

        List<UserBean> userBeans = userMapper.selectList(
                new QueryWrapper<UserBean>()
                        .select("id", "user_name", "portrait", "nick_name")
                        .like("user_name", userName)
                        .ne("id", LocalUser.get().getId()));
        if(userBeans.size() == 0){
            return Result.error("找不到用户");
        }

        return Result.ok(userBeans);
    }

    /**
     * 更换头像
     * @param file
     * @return
     */
    @Override
    public Result setPic(MultipartFile file) {
        if(Objects.isNull(file)){
            return Result.error("上传错误");
        }
        String picPath = null;
        try {
            picPath = UpImgUtil.upImg(file, path);
        }catch (Exception e){
            e.printStackTrace();
            return Result.error("上传错误");
        }
        if(StringUtils.isEmpty(picPath)){
            return Result.error("更换失败");
        }

        int index = userMapper.updateById(new UserBean().setId(LocalUser.get()
                .getId()).setPortrait(picPath));
        if(index == 0){
            return Result.error("用户信息更新失败");
        }
        UserVo userVo = LocalUser.get();
        userVo.setPortrait(picPath);
        String userInfo = JsonMapper.toJson(userVo);
        redis.opsForValue().set(userVo.getToken(),
                userInfo, 3600 * 24 * 30 * 6, TimeUnit.SECONDS);
        return Result.okMsg("更换成功");
    }

    @Override
    public Result getMyInfo() {

        return Result.ok(LocalUser.get());
    }


}
