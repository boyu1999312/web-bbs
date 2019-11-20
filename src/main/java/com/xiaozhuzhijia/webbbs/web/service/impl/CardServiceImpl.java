package com.xiaozhuzhijia.webbbs.web.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.xiaozhuzhijia.webbbs.common.constant.XZZJFinal;
import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.entity.CardBean;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import com.xiaozhuzhijia.webbbs.common.util.TokenUtil;
import com.xiaozhuzhijia.webbbs.common.util.UpImgUtil;
import com.xiaozhuzhijia.webbbs.common.vo.UserVo;
import com.xiaozhuzhijia.webbbs.web.local.LocalUser;
import com.xiaozhuzhijia.webbbs.web.mapper.CardMapper;
import com.xiaozhuzhijia.webbbs.web.service.CardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
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
    public Result addCard(CardDto cardDto, MultipartFile file,
                          HttpServletRequest request) {

        String token = (String) request.getSession()
                .getAttribute(LocalUser.get().getId() + XZZJFinal.ADDCARD_TOKEN);

        System.out.println(cardDto.getToken() + "|" + token);
        if(!TokenUtil.equalsToken(token, cardDto.getToken())){
            return Result.error("请勿重复添加卡片");
        }
        request.getSession().removeAttribute(LocalUser.get().getId() +
                XZZJFinal.ADDCARD_TOKEN);
        if(!cardDto.isNull()){
            return Result.error("请检查填写是否有误");
        }
        Result result = Result.okMsg("已添加一个任务卡片");


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
            String filePath = UpImgUtil.upImg(file, path);
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

    /**
     * 获取token
     * @param request
     * @return
     */
    @Override
    public Result getAddCardToken(HttpServletRequest request) {

        Integer id = LocalUser.get().getId();
        String toekn = TokenUtil.getToekn(id);
        request.getSession().setAttribute(id + XZZJFinal.ADDCARD_TOKEN, toekn);
        return Result.ok(toekn);
    }

    /**
     * 根据ID删除卡片
     * @param id
     * @return
     */
    @Override
    public Result delCard(Integer id) {
        int index = cardMapper.deleteById(new CardBean().setId(id));
        if(index == 0){
            return Result.error("卡片删除失败");
        }
        return Result.okMsg("卡片删除成功");
    }

}
