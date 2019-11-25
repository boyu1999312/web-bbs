package com.xiaozhuzhijia.webbbs.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaozhuzhijia.webbbs.common.entity.FriendRequestBean;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class FriendNoticeVo {

    private Integer id;
    /** 我的昵称 */
    private String myNickname;
    /** 对方昵称 */
    private String otherNickname;
    /** 发起时间 */
    @JsonFormat(pattern="yyyy-MM-dd HH:mm",timezone = "GMT+8")
    private Date time;
    /** 我是否为请求人 */
    private Boolean isOriginator;
    /** 附加消息 */
    private String message;
    /** 结果 */
    private Integer result;

    /**
     * 将结果添加到Vo中
     * @param requestBean
     * @return
     */
    public FriendNoticeVo addFriendRequestBean
            (FriendRequestBean requestBean, Integer id){

        return this.setId(requestBean.getId())
                .setMyNickname(requestBean.getUserNickname())
                .setOtherNickname(requestBean.getOtherNickname())
                .setIsOriginator(requestBean.isOriginator(id))
                .setMessage(requestBean.getMessage())
                .setTime(requestBean.getCreatedTime())
                .setResult(requestBean.getState());
    }
}
