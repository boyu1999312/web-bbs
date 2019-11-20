package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.entity.CardBean;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.common.util.upImgUtil;
import com.xiaozhuzhijia.webbbs.common.vo.UserVo;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import com.xiaozhuzhijia.webbbs.web.mapper.CardMapper;
import com.xiaozhuzhijia.webbbs.web.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

        if(!cardDto.isNull()){
            return Result.error("请检查填写是否有误");
        }

        CardBean cardBean = new CardBean().setUserId(LocalUser.get().getId())
                .setCardSuperintendent(cardDto.getCheckUserName())
                .setCardTitle(cardDto.getTitle())
                .setCardTime(cardDto.getTime())
                .setCardMsg(cardDto.getMsg())
                .setCardEffect(2)
                .setCreatedTime(new Date());
        cardBean.setUpdatedTime(cardBean.getCreatedTime());

        //判断是否上传了图片
        if(!Objects.isNull(file)){

            if(StringUtils.isEmpty(path)){
                path = CardServiceImpl.class.getClassLoader().getResource("static/images/").getPath();
            }
            //调用图片上传工具
            String filePath = upImgUtil.upImg(file, path);
            if(StringUtils.isEmpty(filePath)){
                result.setCode(302);
                result.setMsg("照片上传失败");
            }

            cardBean.setCardPic(filePath);
        }
        cardMapper.insert(cardBean);
        return result;
    }

    /**
     * 获取自己创建的卡片
     * @return
     */
    @Override
    public Result getMyCard() {

        UserVo userVo = LocalUser.get();
        List<CardBean> cardBeans = cardMapper.selectList(
                new QueryWrapper<CardBean>()
                        .eq("user_id", userVo.getId())
                        .eq("card_effect", 2));
        if(Objects.isNull(cardBeans) || cardBeans.size() == 0){
            return Result.error("未找到您任何进行中的卡片");
        }
        List<CardDto> cardDtos = new ArrayList<>();
        for (CardBean cardBean : cardBeans) {
            cardDtos.add(new CardDto().toCardDto(cardBean));
        }
        System.out.println(cardDtos);
        return Result.ok(cardDtos);
    }

}
