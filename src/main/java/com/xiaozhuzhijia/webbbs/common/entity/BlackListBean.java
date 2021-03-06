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
@TableName("black_list")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class BlackListBean {


    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 本用户ID */
    private Integer userId;
    /** 其他用户ID */
    private Integer otherId;
    /** 1-拉黑 2-解除 */
    private Integer state;
    /** 注册时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 修改次数 */
    private Integer version;

    /** 拉黑状态 */
    @TableField(exist = false)
    private static final int BLACK = 1;

    /**
     * 是否拉黑
     * @return
     */
    public boolean isBlack(){
        return this.state == BlackListBean.BLACK;
    }

}
