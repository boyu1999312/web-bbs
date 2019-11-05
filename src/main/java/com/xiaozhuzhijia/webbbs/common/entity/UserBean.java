package com.xiaozhuzhijia.webbbs.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@TableName("xzzj_user")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class UserBean {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 用户名 */
    private String userName;
    /** 昵称 */
    private String nickName;
    /** 密码 */
    private String passWord;
    /** 性别 */
    private Integer gender;
    /** 邮箱 */
    private String email;
    /** 注册时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 修改次数 */
    private Integer version;
    /** 头像 */
    private String portrait;
    /** 盐值 */
    private String salt;




}
