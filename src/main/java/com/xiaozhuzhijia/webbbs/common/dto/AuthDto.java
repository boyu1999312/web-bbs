package com.xiaozhuzhijia.webbbs.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 用于xzzj注册登录
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AuthDto {

    //注册
    /** 用户名或邮箱 */
    private String accEmail;
    /** 密码 */
    private String lPwd;

    //登录
    /** 用户名 */
    private String acc;
    /** 邮箱 */
    private String email;
    /** 邮件验证码 */
    private String code;
    /** 密码 */
    private String pwd;
    /** 重复密码 */
    private String rePwd;
    /** 性别 */
    private Integer gender;
    /** 注册时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date upDatedTime;
    /** 验证码token */
    private String codeCache;
}
