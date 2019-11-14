package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.web.service.CardService;
import com.xiaozhuzhijia.webbbs.web.service.impl.CardServiceImpl;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/xzzj/bbs/card")
public class CardController {

    @Autowired
    private CardService cardService;


    private Log log = LogFactory.getLog(CardController.class);


    @RequestMapping("/addCard")
    public Result add(CardDto cardDto, @RequestParam("pic") MultipartFile file){

        log.info("卡片DTO信息：" + cardDto);

        return cardService.addCard(cardDto, file);
    }
}
