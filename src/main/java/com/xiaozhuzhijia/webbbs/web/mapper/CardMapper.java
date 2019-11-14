package com.xiaozhuzhijia.webbbs.web.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xiaozhuzhijia.webbbs.common.entity.CardBean;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

@Mapper
@Component
public interface CardMapper extends BaseMapper<CardBean> {
}
