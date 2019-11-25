package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.entity.FriendBean;
import com.xiaozhuzhijia.webbbs.common.util.IntegerUtils;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.common.vo.UserVo;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import com.xiaozhuzhijia.webbbs.web.mapper.FriendMapper;
import com.xiaozhuzhijia.webbbs.web.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.print.attribute.standard.MediaSize;
import java.util.Objects;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendMapper friendMapper;
    /**
     * 添加好友
     * @param id
     * @return
     */
    @Override
    public Result addFriend(Integer id) {

        if(IntegerUtils.NumIsEmpty(id)){
            return Result.error("添加失败，请重试");
        }

        UserVo userVo = LocalUser.get();
        FriendBean my = new FriendBean().setUserId(userVo.getId())
                .setOtherId(id);
        FriendBean other = new FriendBean().setUserId(id)
                .setOtherId(userVo.getId());
        //查询自己的好友记录
        FriendBean myBean = friendMapper.selectOne(new QueryWrapper<>(my));
        FriendBean otherBean = friendMapper.selectOne(new QueryWrapper<>(other));
        // 如果两个人都没有建立关系
        if(null == myBean && null == otherBean){
            my.setState(2);
            other.setState(2);
            friendMapper.insert(my);
            friendMapper.insert(other);
            return Result.ok("好友请求发送成功");
        }

        if(Objects.isNull(otherBean)){
            if(Objects.isNull(CheckState(otherBean.getState()))){
                // friendMapper.update()
            };
        }



        return null;
    }
    private Result CheckState(Integer state){
        if(state == 1){
            return Result.error("你们已经是好友关系了");
        }
        if(state == 2){
            return Result.error("请不要重复添加好友");
        }
        return null;
    }


}
