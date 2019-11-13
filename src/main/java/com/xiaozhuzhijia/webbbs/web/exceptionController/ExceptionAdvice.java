package com.xiaozhuzhijia.webbbs.web.exceptionController;


import com.xiaozhuzhijia.webbbs.common.util.Result;
import org.springframework.dao.QueryTimeoutException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.ConnectException;

@RestControllerAdvice
public class ExceptionAdvice {

    private final String queryTimeoutException = "服务器发生错误，请联系网站管理员";
    @ExceptionHandler({QueryTimeoutException.class, ConnectException.class})
    public Result exceptionHandler(){

        System.out.println("进入全局异常处理");
        return Result.error(queryTimeoutException);
    }
}
