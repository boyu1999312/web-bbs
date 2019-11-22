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
    /** 注册时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 修改次数 */
    private Integer version;

}
