package com.xiaozhuzhijia.webbbs.web.controller;

import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.web.service.CardService;
import com.xiaozhuzhijia.webbbs.web.service.impl.CardServiceImpl;
import org.apache.juli.logging.Log;
import org.apache.juli.logging.LogFactory;
import org.omg.CORBA.PUBLIC_MEMBER;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/xzzj/bbs/card")
public class CardController {

    @Autowired
    private CardService cardService;


    private Log log = LogFactory.getLog(CardController.class);


    @PostMapping("/addCard")
    public Result add(CardDto cardDto,
                      @RequestParam(value = "filePic", required = false) MultipartFile file,
                      HttpServletRequest request){

        log.info("卡片DTO信息：" + cardDto + "\r\n" + "图片信息：" + file);
        return cardService.addCard(cardDto, file, request);
    }

    @GetMapping("/getMyCard")
    public Result getMyCard(){

        return cardService.getMyCard();
    }

    @GetMapping("/getAddCardToken")
    public Result getAddCardToken(HttpServletRequest request){

        return cardService.getAddCardToken(request);
    }

    @PostMapping("/delCard")
    public Result delCard(Integer id){

        return cardService.delCard(id);
    }
}
