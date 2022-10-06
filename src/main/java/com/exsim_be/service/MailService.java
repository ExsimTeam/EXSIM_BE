package com.exsim_be.service;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
public interface MailService {
    void sendMail(String email,String verificationCode);
}
