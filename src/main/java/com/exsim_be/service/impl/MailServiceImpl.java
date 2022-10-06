package com.exsim_be.service.impl;


import com.exsim_be.service.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-09-15
 */
@Service
public class MailServiceImpl implements MailService {

    @Autowired
    private JavaMailSender mailSender;

    @Value("${spring.mail.from}")
    private String mailFrom;

    @Override
    public void sendMail(String email,String verificationCode) {
        StringBuilder content=new StringBuilder();

        content.append("EXSIM验证码：");
        content.append(verificationCode);
        content.append(",有效期8分钟");

        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(mailFrom);
        message.setTo(email);
        message.setSubject("激活EXSIM");
        message.setText(content.toString());
        mailSender.send(message);
    }
}
