package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/xzzj/bbs/account/card")
public class CardController {

    @RequestMapping("/add")
    public Result add(CardDto cardDto){

        return Result.ok();
    }
}
