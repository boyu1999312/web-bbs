package com.xiaozhuzhijia.webbbs.web.service;

import com.xiaozhuzhijia.webbbs.common.util.Result;

public interface FriendService {

    Result addFriend(Integer id, String nickName);

    Result getMyFriendNotice();

    Result getMyInvalidFriendNotice();

    Result answer(Integer id,Boolean res);

}
