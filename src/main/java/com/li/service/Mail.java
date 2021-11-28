package com.li.service;

import com.li.dao.Config;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

public class Mail {
    private Mail() {
    }

    public static void sendMail(String email, String subject, String emailMsg) {
        Properties p = Config.getProperties();

        Properties props = new Properties();
        props.setProperty("mail.host", p.getProperty("mail.host"));
        props.setProperty("mail.smtp.auth", p.getProperty("mail.smtp.auth"));


        Authenticator authenticator = new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(p.getProperty("mailServer"), p.getProperty("passwd"));
            }
        };

        Session session = Session.getDefaultInstance(props, authenticator);


        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(p.getProperty("mailServer")));
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setContent(emailMsg, "text/html;charset=utf-8");

            Transport.send(message);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
