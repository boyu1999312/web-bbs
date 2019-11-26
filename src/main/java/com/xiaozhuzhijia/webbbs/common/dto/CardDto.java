package com.xiaozhuzhijia.webbbs.common.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaozhuzhijia.webbbs.common.entity.CardBean;
import com.xiaozhuzhijia.webbbs.common.util.IntegerUtils;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.util.StringUtils;

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

    /** 卡片ID */
    private Integer id;
    /** 监督人id */
    private Integer checkUserId;
    /** 任务标题 */
    private String title;
    /** 任务到期时间 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm")
    private Date time;
    /** 任务审核时间 */
    private Date examineTime;
    /** 任务简介 */
    private String msg;
    /** 任务图片 */
    private String pic;
    /** 创建时间 */
    private Date createdTime;
    /** 修改时间 */
    private Date updatedTime;
    /** 任务状态 */
    private Integer state;
    /** 验证token */
    private String token;

    /** 检查必填项是否为空 */
    public boolean isNull(){

        return  StringUtils.isEmpty(this.getTitle()) ||
        IntegerUtils.NumIsEmpty(this.getCheckUserId()) ||
        Objects.isNull(this.getTime());
    }

    public CardDto toCardDto(CardBean cardBean){

        return this.setId(cardBean.getId())
                .setTitle(cardBean.getCardTitle())
                .setTime(cardBean.getCardTime())
                .setCheckUserId(cardBean.getCardSuperintendent())
                .setMsg(cardBean.getCardMsg())
                .setPic(cardBean.getCardPic())
                .setState(cardBean.getCardEffect())
                .setCreatedTime(cardBean.getCreatedTime())
                .setUpdatedTime(cardBean.getUpdatedTime())
                .setExamineTime(cardBean.getCardExamineTime());
    }

}
