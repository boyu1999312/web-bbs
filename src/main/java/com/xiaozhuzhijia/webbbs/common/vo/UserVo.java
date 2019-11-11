package com.xiaozhuzhijia.webbbs.common.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

/** 前后端信息传递 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class UserVo {

    /** 邮箱 */
    private String email;
    /** 昵称 */
    private String nickName;
    /** 用户民 */
    private String userName;
    /** 注册时间 */
    private Date createdTime;
    /** 登陆时间 */
    private Date loginTime;
    /** 在线密匙 */
    private String token;
    /** 头像 */
    private String portrait;

}
