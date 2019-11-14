package com.xiaozhuzhijia.webbbs.web.service;

import com.xiaozhuzhijia.webbbs.common.dto.CardDto;
import com.xiaozhuzhijia.webbbs.common.util.Result;
import org.springframework.web.multipart.MultipartFile;

public interface CardService {

    Result addCard(CardDto cardDto, MultipartFile file);

}
