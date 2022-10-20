package com.exsim_be.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.TimeUnit;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-20
 */
@Component
public class ThreadService {

    @Autowired
    MailService mailService;
    @Async("taskExecutor")
    public void sendEmail(String email,String verifyCode){
        mailService.sendMail(email,verifyCode);
    }

}
