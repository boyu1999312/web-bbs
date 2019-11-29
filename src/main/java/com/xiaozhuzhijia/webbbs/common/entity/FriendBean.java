package com.xiaozhuzhijia.webbbs.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.util.Date;


@Data
@TableName("xzzj_friend")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FriendBean implements Serializable {


    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 本用户Id */
    private Integer userId;
    /** 好友用户名 */
    private Integer otherId;
    /** 对本用户备注 */
    private String userRemarks;
    /** 对好友用户备注 */
    private String otherRemarks;
    /** 成为好友时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 好友状态 true-好友 false-陌生人 */
    private Boolean state;
    /** 友好关系 */
    private Integer friendlyLevel;
    /** 权限 1-允许看卡片 2-不允许看卡片 */
    private Integer jurisdiction;
    /** 组 */
    private Integer friendsGroup;
    /** 修改次数 */
    private Integer version;

    /***
     * 是否为好友
     * @return
     */
    public boolean isFriend(){

        return this.state == null ? false : this.state;
    }
}
