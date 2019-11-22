package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.web.service.FriendService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xzzj/bbs/account/friend")
public class FriendController {

    @Autowired
    private FriendService friendService;

    @PostMapping("addFriend")
    public Result addFriend(Integer id){

        return friendService.addFriend(id);
    }

}
