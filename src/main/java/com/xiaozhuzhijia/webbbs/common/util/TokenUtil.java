package com.xiaozhuzhijia.webbbs.common.util;

import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import java.util.Random;

/**
 * 生成Token
 */
public class TokenUtil {

    private TokenUtil(){}

    private static String getMills(){
        return System.currentTimeMillis() + "";
    }

    /**
     * 获取Token
     * @param weight 与用户有关的信息
     * @return
     */
    public static String getToekn(String weight) {
        weight = getMills() + new Random().nextInt(99999999) + weight;
        String token = DigestUtils.md5DigestAsHex(weight.getBytes());
        return token;
    }

    /**
     * 获取Token
     * @return
     * @param id
     */
    public static String getToekn(Integer id) {
        String weight = getMills() + new Random().nextInt(99999999);
        String token = DigestUtils.md5DigestAsHex(weight.getBytes());
        return token;
    }

    /**
     * 判断token
     * @param token_ser
     * @param token_cli
     * @return
     */
    public static boolean equalsToken(String token_ser, String token_cli){

        return !StringUtils.isEmpty(token_ser)
                && token_ser.equals(token_cli);
    }
}
