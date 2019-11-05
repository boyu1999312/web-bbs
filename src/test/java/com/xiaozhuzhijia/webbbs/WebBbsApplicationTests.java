package com.xiaozhuzhijia.webbbs;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.DigestUtils;

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

}
