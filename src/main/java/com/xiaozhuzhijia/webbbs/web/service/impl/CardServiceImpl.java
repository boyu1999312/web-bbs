package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.entity.CardBean;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.common.util.upImgUtil;
import com.xiaozhuzhijia.webbbs.web.controller.CardController;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import com.xiaozhuzhijia.webbbs.web.mapper.CardMapper;
import com.xiaozhuzhijia.webbbs.web.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.net.URL;
import java.util.Date;
import java.util.Objects;

@Service
public class CardServiceImpl implements CardService {

    @Autowired
    private CardMapper cardMapper;
    @Value("${xzzj.imgPath}")
    private String path;
    /**
     * 新增任务卡片
     * @param cardDto
     * @param file
     * @return
     */
    @Override
    public Result addCard(CardDto cardDto, MultipartFile file) {


        Result result = Result.ok("已添加一个任务卡片");
        if(cardDto.isNull()){
            return Result.error("请检查填写是否有误");
        }

        CardBean cardBean = new CardBean().setUserId(LocalUser.get().getId())
                .setCardSuperintendent(cardDto.getCheckUserName())
                .setCardTitle(cardDto.getTitle())
                .setCardTime(cardDto.getTime())
                .setCardMsg(cardDto.getMsg())
                .setCardEffect(2)
                .setCreatedTime(new Date());

        //判断是否上传了图片
        if(!Objects.isNull(file)){

            if(StringUtils.isEmpty(path)){
                path = CardServiceImpl.class.getClassLoader().getResource("static/images/").getPath();
                System.out.println(path);
            }
            //调用图片上传工具
            String filePath = upImgUtil.upImg(file, path);
            System.out.println(filePath);
            if(StringUtils.isEmpty(filePath)){
                result.setCode(302);
                result.setMsg("照片上传失败");
            }

            cardBean.setCardPic(filePath);
        }

        // cardMapper.insert(cardBean);
        return result;
    }


}
