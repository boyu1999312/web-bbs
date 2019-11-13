package com.xiaozhuzhijia.webbbs.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;



/**
 * 用于任务卡片的数据交互
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CardDto {


    /** 监督人id */
    private Integer checkUserId;
    /** 任务标题 */
    private String title;
    /** 任务到期时间 */
    private Date time;
    /** 任务简介 */
    private String msg;
    /** 任务图片 */
    private String pic;
    /** 创建时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;

}
