package com.xiaozhuzhijia.webbbs.common.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

import java.io.File;
import java.util.Date;
import java.util.Objects;


/**
 * 用于任务卡片的数据交互
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CardDto {


    /** 监督人id */
    private String checkUserName;
    /** 任务标题 */
    private String title;
    /** 任务到期时间 */
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;
    /** 任务审核时间 */
    private Date examineTime;
    /** 任务简介 */
    private String msg;
    /** 任务图片 */
    private String cardPic;
    /** 创建时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 任务状态 */
    private Integer state;

    public boolean isNull(){

        return !StringUtils.isEmpty(this.getMsg()) &&
                !StringUtils.isEmpty(this.getTitle()) &&
                !StringUtils.isEmpty(this.getCheckUserName()) &&
                !Objects.isNull(this.getTime());
    }

}
