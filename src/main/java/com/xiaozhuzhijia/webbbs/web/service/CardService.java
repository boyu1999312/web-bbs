package com.xiaozhuzhijia.webbbs.web.service;

import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

public interface CardService {

    Result addCard(CardDto cardDto, MultipartFile file, HttpServletRequest request);

    Result getMyCard();

    Result getAddCardToken(HttpServletRequest request);

    Result delCard(Integer id);

}
