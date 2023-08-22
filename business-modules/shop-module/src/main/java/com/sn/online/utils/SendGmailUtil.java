package com.sn.online.utils;


import javax.mail.*;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Date;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SendGmailUtil {
    /*
     * gmail邮箱SSL方式
     */
    private static void gmailssl(Properties props) {
        final String SSL_FACTORY = "javax.net.ssl.SSLSocketFactory";
        props.put("mail.debug", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.ssl.enable", "true");
        props.put("mail.smtp.socketFactory.class", SSL_FACTORY);
        props.put("mail.smtp.port", "465");
        props.put("mail.smtp.socketFactory.port", "465");
        props.put("mail.smtp.auth", "true");
    }


    //gmail邮箱的TLS方式
    private static void gmailtls(Properties props) {
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");
    }

    /**
     *
     * @param username  发送者账号
     * @param password  发送者密码，不是邮箱密码
     * @param email   接受者邮箱
     * @param subject  主题
     * @param text  正文
     */
    public static void gmailSender(String username,String password, String email, String subject, String text) {

        // Get a Properties object
        Properties props = new Properties();
        //选择ssl方式
        gmailssl(props);

       /* final String username = "GloriazMcCaben24@gmail.com";//gmail邮箱
        final String password = "hunnntygbcshynvr";//密码*/
        Session session = Session.getDefaultInstance(props,
                new Authenticator() {
                    protected javax.mail.PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });
        // -- Create a new message --
        Message msg = new MimeMessage(session);
        // -- Set the FROM and TO fields --
        try {
            msg.setFrom(new InternetAddress(username));
            msg.setRecipients(Message.RecipientType.TO,
                    InternetAddress.parse(email));
            msg.setSubject(subject);
            msg.setText(text);
            msg.setSentDate(new Date());
            Transport.send(msg);
        } catch (AddressException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
