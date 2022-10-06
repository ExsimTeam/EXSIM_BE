package com.exsim_be.service;

import com.exsim_be.entity.User;
import com.exsim_be.vo.returnVo.Result;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
public interface AuthService {
    Result login(String email,String password);

    void sendVerify(String email);

    Result setPassword(String email,String newPassword,String verificationCode);
    Result logout(String token);

    Result register(String username, String email, String password, String verify);

    User checkToken(String token);

}
