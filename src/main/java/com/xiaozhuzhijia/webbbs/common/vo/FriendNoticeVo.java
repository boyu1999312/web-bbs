package com.xiaozhuzhijia.webbbs.common.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.xiaozhuzhijia.webbbs.common.entity.FriendRequestBean;
import com.xiaozhuzhijia.webbbs.common.entity.UserBean;
import com.xiaozhuzhijia.webbbs.common.util.AuthSelect;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
     * @param req
     * @return
     */
    public FriendNoticeVo addFriendRequestBean
            (FriendRequestBean req, UserVo userVo){
        boolean isOriginator = req.isOriginator(userVo.getId());
        UserBean reqBean = AuthSelect.getUser(isOriginator ? req.getOtherId() : req.getUserId());
        return this.setId(req.getId())
                .setMyNickname(userVo.getNickName())
                .setOtherNickname(reqBean.getNickName())
                .setIsOriginator(isOriginator)
                .setMessage(req.getMessage())
                .setTime(req.getCreatedTime())
                .setResult(req.getState());
    }

    /** 加入集合 */
    public static List<FriendNoticeVo> toVos(List<FriendRequestBean> beans, UserVo userVo){
        List<FriendNoticeVo> vos = new ArrayList<>();
        for (FriendRequestBean bean : beans) {
            vos.add(new FriendNoticeVo().addFriendRequestBean(bean, userVo));
        }
        return vos;
    }

}
