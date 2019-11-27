package com.xiaozhuzhijia.webbbs;

import com.xiaozhuzhijia.webbbs.common.util.DateUtils;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

import java.util.Date;

// @SpringBootTest
class WebBbsApplicationTests {

    @Test
    void contextLoads() {
        String salt = "1572942387095";
        String pwd = "123456789a";
        String hex = DigestUtils.md5DigestAsHex((pwd + salt).getBytes());
        String hex2 = DigestUtils.md5DigestAsHex((salt + pwd).getBytes());
        System.out.println(hex + "\r\n" + hex2);
    }

    @Test
    void test01(){
        if("/xzzj/bbs/account/login".contains("/xzzj/bbs/account")) {
            System.out.println("存在");
        }else {
            System.out.println("不存在");
        }
    }

    @Test
    void test02(){
        String pastString = DateUtils.getString(7, true);
        System.out.println(pastString);
        System.out.println(DateUtils.getString(7));
        Date date = DateUtils.getDate(7);
        Date date1 = new Date();
        boolean before = date1.before(date);
        System.out.println(before);
    }

}
