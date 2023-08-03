package com.sn.online.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

@Component
public class SendGmail {

    @Autowired
    private JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String from;

    /**
     to:接收方
     from:发送方
     subject:邮件标题
     text:邮件内容
     **/
    public void sendEmai(int randomNumber,String toForm){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(from);
        message.setSubject("测试邮件标题");
        message.setTo(toForm);
        message.setText("测试邮件类容"+randomNumber);
        javaMailSender.send(message);
    }
}
