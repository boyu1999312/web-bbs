package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.anno.RedisCache;
import com.xiaozhuzhijia.webbbs.common.entity.BlackListBean;
import com.xiaozhuzhijia.webbbs.common.entity.FriendBean;
import com.xiaozhuzhijia.webbbs.common.entity.FriendRequestBean;
import com.xiaozhuzhijia.webbbs.common.enu.CachePre;
import com.xiaozhuzhijia.webbbs.common.enu.CacheType;
import com.xiaozhuzhijia.webbbs.common.util.DateUtils;
import com.xiaozhuzhijia.webbbs.common.util.IntegerUtils;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.common.vo.FriendNoticeVo;
import com.xiaozhuzhijia.webbbs.common.vo.UserVo;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import com.xiaozhuzhijia.webbbs.web.mapper.BlackListMapper;
import com.xiaozhuzhijia.webbbs.web.mapper.FriendMapper;
import com.xiaozhuzhijia.webbbs.web.mapper.FriendRequestMapper;
import com.xiaozhuzhijia.webbbs.web.mapper.UserMapper;
import com.xiaozhuzhijia.webbbs.web.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class FriendServiceImpl implements FriendService {

    @Autowired
    private FriendMapper friendMapper;
    @Autowired
    private FriendRequestMapper friendRequestMapper;
    @Autowired
    private BlackListMapper blackListMapper;
    @Autowired
    private UserMapper userMapper;
    /**
     * 添加好友
     * @param id
     * @return
     */
    @Override
    public Result addFriend(Integer id, String otherRemarks, String message) {

        if(IntegerUtils.isEmpty(id)){
            return Result.error("添加失败，请重试");
        }

        UserVo userVo = LocalUser.get();
        if(id.equals(userVo.getId())){
            return Result.error("不能添加自己为好友");
        }
        // 查询黑名单
        BlackListBean myBlack = blackListMapper.selectOne(
                new QueryWrapper<>(new BlackListBean()
                        .setUserId(userVo.getId()).setOtherId(id)));
        BlackListBean otherBlack = blackListMapper.selectOne(
                new QueryWrapper<>(new BlackListBean()
                .setUserId(id).setOtherId(userVo.getId())));

        if(!Objects.isNull(otherBlack) && otherBlack.isBlack()){
            return Result.error("对方拒绝添加您为好友");
        }
        if(!Objects.isNull(myBlack) && myBlack.isBlack()){
            return Result.error("请先解除黑名单状态");
        }


        // 查询好友记录
        FriendBean friendBean = friendMapper.selectOne(
                new QueryWrapper<FriendBean>()
                        .eq("user_id", userVo.getId())
                        .eq("other_id", id)
                        .or()
                        .eq("user_id", id)
                        .eq("other_id", userVo.getId()));

        // 查询申请记录
        FriendRequestBean friendRequest = friendRequestMapper.selectOne(
                new QueryWrapper<FriendRequestBean>()
                        .eq("user_id", userVo.getId())
                        .eq("other_id", id)
                        .eq("state", true)
                        .or()
                        .eq("user_id", id)
                        .eq("other_id", userVo.getId())
                        .eq("state", true));
        // 准备好友记录
        FriendRequestBean request = new FriendRequestBean().setUserId(userVo.getId())
                .setOtherId(id).setState(FriendRequestBean.EFFECT)
                .setOtherRemarks(otherRemarks)
                .setMessage(message)
                .setCreatedTime(new Date())
                .setOverduedTime(DateUtils.getDate(7));
        request.setUpdatedTime(request.getCreatedTime());
        // 如果两个人都没有建立关系
        if(Objects.isNull(friendBean)){

            if(!Objects.isNull(friendRequest)){
                if(friendRequest.isOriginator(userVo.getId())){
                    return Result.error("请勿重复添加好友");
                }
                return Result.error("对方已经向您发起了好友申请，请勿重复添加");
            }

            int index = friendRequestMapper.insert(request);
            if(index != 1){
                return Result.error("好友请求失败，请重试");
            }
            return Result.okMsg("好友请求发送成功");
        }

        // 先不考虑数据被破坏删除丢失的因素
        // if(Objects.isNull(otherBean)){
        //     if(Objects.isNull(CheckState(otherBean.getState()))){
        //         // friendMapper.update()
        //     };
        // }
        // 是否为好友关系和待验证关系

        if(friendBean.isFriend()){
            return Result.error("您和对方已经是好友关系");
        }
        if(Objects.isNull(friendRequest)){
            if(!friendBean.isFriend()){
                int insert = friendRequestMapper.insert(request);
                if(insert == 1){
                    return Result.okMsg("添加成功");
                }
                return Result.error("好友请求失败，请重试");
            }
        }

        if(friendRequest.isOriginator(userVo.getId())){
            return Result.error("您已发出了好友申请，请等待回应");
        }
        return Result.error("对方已经向您发出了好友申请，请勿重复添加");

    }

    /***
     * 获取自己的好友通知
     * @return
     */
    @Override
    public Result getMyFriendNotice() {

        UserVo userVo = LocalUser.get();
        List<FriendRequestBean> requestBeans = friendRequestMapper.selectList(
                new QueryWrapper<FriendRequestBean>()
                        .eq("user_id", userVo.getId())
                        .eq("state", FriendRequestBean.EFFECT)
                        .or()
                        .eq("other_id", userVo.getId())
                        .eq("state", FriendRequestBean.EFFECT)
                        .orderByDesc("created_time"));
        List<FriendNoticeVo> friendNoticeVos = new ArrayList<>();
        if(requestBeans.size() == 0){
            return Result.error("没有通知");
        }
        for (FriendRequestBean requestBean : requestBeans) {
            // 判断这条好友申请是否过期
            boolean before = requestBean.getCreatedTime()
                    .before(requestBean.getOverduedTime());
            if(!before){
                requestBean.setState(4);
                // 更新数据
                friendRequestMapper.update(requestBean,
                        new QueryWrapper<FriendRequestBean>()
                                .eq("id", requestBean.getId()));
            }
            friendNoticeVos.add(new FriendNoticeVo()
                    .addFriendRequestBean(requestBean, userVo));
        }
        return Result.ok(friendNoticeVos);
    }

    /***
     * 查询失效的好友申请
     * @return
     */
    // @RedisCache(value = CachePre.FRIEND_NOTICE,
    //         cla = FriendRequestBean.class)
    @Override
    public Result getMyInvalidFriendNotice() {

        UserVo userVo = LocalUser.get();
        List<FriendRequestBean> requestBeans = friendRequestMapper.selectList(
                new QueryWrapper<FriendRequestBean>()
                        .eq("user_id", userVo.getId())
                        .ne("state", FriendRequestBean.EFFECT)
                        .or()
                        .eq("other_id", userVo.getId())
                        .ne("state", FriendRequestBean.EFFECT));
        if(requestBeans.size() == 0){
            return Result.error();
        }
        return Result.ok(FriendNoticeVo.toVos(requestBeans, userVo));
    }

    /***
     * 同意或拒绝
     * @return
     */
    // @RedisCache(value = CachePre.FRIEND_NOTICE,
    //         type = CacheType.UPDATE,
    //         cla = FriendRequestBean.class)
    @Override
    public Result answer(Integer id,Boolean res) {
        if(IntegerUtils.isEmpty(id) || null == res){
            return Result.error("操作无效，请重试");
        }

        UserVo userVo = LocalUser.get();
        FriendRequestBean friendRequestBean = friendRequestMapper.selectOne(new QueryWrapper<FriendRequestBean>()
                .eq("id", id)
                .eq("other_id", userVo.getId()));
        if(Objects.isNull(friendRequestBean)){
            return Result.error("操作无效，请重试");
        }
        friendRequestBean.setState(res ? 2 : 3);
        int index = friendRequestMapper.update(friendRequestBean,
                new QueryWrapper<FriendRequestBean>()
                        .eq("id", id));
        if(index == 0){
            return Result.error("操作无效，请重试");
        }
        //写入好友
        if(res){
            new FriendBean()
                    .setUserId(friendRequestBean.getUserId())
                    .setOtherId(friendRequestBean.getOtherId())
                    .setState(true)
                    .setCreatedTime(new Date());
        }
        return Result.ok();
    }

}
