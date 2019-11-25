package com.xiaozhuzhijia.webbbs.common.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;


@Data
@TableName("xzzj_friend_request")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FriendRequestBean {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 用户ID */
    private Integer userId;
    /** 其他用户ID */
    private Integer otherId;
    /** 用户昵称 */
    private String userNickname;
    /** 其他用户昵称 */
    private String otherNickname;
    /** 附加消息 */
    private String message;
    /** 是否生效 1-进行中 2-同意 3-拒绝 */
    private Integer state;
    /** 成为好友时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 版本 */
    private Integer version;

    /** 表示记录生效 */
    @TableField(exist = false)
    public static final int EFFECT = 1;

    /**
     * 是否为发起人
     * @return
     */
    public boolean isOriginator(Integer id){
        return this.userId.equals(id);
    }

}
