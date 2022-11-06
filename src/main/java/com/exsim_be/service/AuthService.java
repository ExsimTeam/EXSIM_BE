package com.exsim_be.service;

import com.exsim_be.entity.User;
import com.exsim_be.vo.returnVo.ResponseResult;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
public interface AuthService {
    ResponseResult login(String email, String password);

    void sendVerify(String email);

    ResponseResult setPassword(String email, String newPassword, String verificationCode);
    ResponseResult logout(String token);

    ResponseResult register(String username, String email, String password, String verify);

    User checkToken(String token);

}
