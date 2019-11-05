package com.xiaozhuzhijia.webbbs.common.util;

import javax.mail.*;
import javax.mail.Message.RecipientType;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class MailUtil {

    private static final String form = "boyu155115@163.com";

    private String toEmail = null;
    private String code = null;

    public MailUtil(String toEmail, String code) {
        this.toEmail = toEmail;
        this.code = code;
    }

    public static boolean send(String toEmail, String code){
        return new MailUtil(toEmail,code).send();
    }

    private boolean send() {
        //0.1 确定连接位置
        Properties props = new Properties();
        //获取163邮箱smtp服务器的地址，
        props.setProperty("mail.host", "smtp.163.com");
        //是否进行权限验证。
        props.setProperty("mail.smtp.auth", "true");


        //0.2确定权限（账号和密码）
        Authenticator authenticator = new Authenticator() {
            @Override
            public PasswordAuthentication getPasswordAuthentication() {
                //填写自己的163邮箱的登录帐号和授权密码，授权密码的获取，在后面会进行讲解。
                return new PasswordAuthentication(form,"kuaile09");
            }
        };

        //1 获得连接
        /**
         * props：包含配置信息的对象，Properties类型
         *         配置邮箱服务器地址、配置是否进行权限验证(帐号密码验证)等
         *
         * authenticator：确定权限(帐号和密码)
         *
         * 所以就要在上面构建这两个对象。
         */

        Session session = Session.getInstance(props,authenticator);

        //2 创建消息
        Message message = new MimeMessage(session);
        // 2.1 发件人        xxx@163.com 我们自己的邮箱地址，就是名称
        try {
            message.setFrom(new InternetAddress(form));
            /**
             * 2.2 收件人
             *         第一个参数：
             *             RecipientType.TO    代表收件人
             *             RecipientType.CC    抄送
             *             RecipientType.BCC    暗送
             *         比如A要给B发邮件，但是A觉得有必要给要让C也看看其内容，就在给B发邮件时，
             *         将邮件内容抄送给C，那么C也能看到其内容了，但是B也能知道A给C抄送过该封邮件
             *         而如果是暗送(密送)给C的话，那么B就不知道A给C发送过该封邮件。
             *     第二个参数
             *         收件人的地址，或者是一个Address[]，用来装抄送或者暗送人的名单。或者用来群发。可以是相同邮箱服务器的，也可以是不同的
             *         这里我们发送给我们的qq邮箱
             */
            message.setRecipient(RecipientType.TO, new InternetAddress(toEmail));
            // 2.3 主题（标题）
            message.setSubject("小猪之家的登门信息");
            // 2.4 正文
            String str = "可爱的用户： <br/>" +
                    "您好，欢迎您在小猪之家注册！<br/>" +
                    "这是新鲜出炉的激活码：<b>" + code.split(",")[1] + "<b/><br/>" +
                    "如果两分钟内没有使用，会失效的哦！<br/>" +
                    "如果没有找到的话！请在垃圾邮件中找找看！麻烦了内！！<br/>"+
                    "如果不是本人，请删除邮件哦！";
            //设置编码，防止发送的内容中文乱码。
            message.setContent(str, "text/html;charset=UTF-8");


            //3发送消息
            Transport.send(message);
            //4通知
            System.out.println("邮件发送成功！");
            return true;
        } catch (MessagingException e) {
            e.printStackTrace();
            System.out.println("邮件发送失败！");
        }
        return false;
    }
}