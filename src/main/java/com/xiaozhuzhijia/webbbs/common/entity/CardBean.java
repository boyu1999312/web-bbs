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
@TableName("xzzj_card")
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class CardBean {

    @TableId(type = IdType.AUTO)
    private Integer id;
    /** 创建人id */
    private Integer userId;
    /** 监督人id */
    private Integer cardSuperintendent;
    /** 任务标题 */
    private String cardTitle;
    /** 任务到期时间 */
    private Date cardTime;
    /** 任务简介 */
    private String cardMsg;
    /** 任务图片 */
    private String cardPic;
    /** 创建时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 状态 1-正在进行 2-完成 3-未完成 4-禁用 */
    private Integer cardEffect;
    /** 修改次数 */
    private Integer version;
}
