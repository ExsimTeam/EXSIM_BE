package com.exsim_be.controller;

import com.exsim_be.service.AuthService;
import com.exsim_be.utils.FormatCheck;
import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import com.exsim_be.vo.paramVo.LoginParam;
import com.exsim_be.vo.paramVo.RegisterParam;
import com.exsim_be.vo.paramVo.SetPasswordParam;
import com.exsim_be.vo.returnVo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
@RequestMapping("/api/auth")
@RestController
public class AuthController {
    @Autowired
    AuthService authService;

    /**
     * @param loginParam
     * @return 100:username or password incorrect
     */
    @PostMapping("/login")
    public ResponseEntity<Result> login( LoginParam loginParam) {
        //检查参数合法性
        if (!loginParam.isLegal()) {
            return ResponseEntity.badRequest()
                    .body(Result.fail(GlobalCodeEnum.PARAMS_ERROR.getCode(), GlobalCodeEnum.PARAMS_ERROR.getMsg()));
        }
        Result result = authService.login(loginParam.getEmail(), loginParam.getPassword());
        return ResponseEntity.ok(result);
    }

    /**
     * @param registerParam
     * @return 100:user has already registered 101:wrong verification code
     */
    @PostMapping("/register")
    public ResponseEntity<Result> register(RegisterParam registerParam) {
        //check params
        if (!registerParam.isLegal()) {
            return ResponseEntity.badRequest()
                    .body(Result.fail(GlobalCodeEnum.PARAMS_ERROR.getCode(), GlobalCodeEnum.PARAMS_ERROR.getMsg()));
        }
        return ResponseEntity.ok(
                authService.register(registerParam.getUsername()
                        , registerParam.getEmail()
                        , registerParam.getPassword(),
                        registerParam.getVerify()));

    }


    /**
     * 发送验证码到邮箱，检查邮箱地址格式，无需关注邮箱地址是否成功发送
     *
     * @param email
     * @return  100: incorrect email format
     */
    @PostMapping("/sendVerify")
    public ResponseEntity<Result> sendVerify(String email) {
        if(!FormatCheck.checkMail(email)){
            return ResponseEntity.badRequest()
                    .body(Result.fail(GlobalCodeEnum.PARAMS_ERROR.getCode(), GlobalCodeEnum.PARAMS_ERROR.getMsg()));
        }
        authService.sendVerify(email);
        return ResponseEntity.ok(Result.succ(null));
    }


    /**
     * 设置密码
     * @param setPasswordParam
     * @return
     */
    @PostMapping("/resetPasswd")
    public ResponseEntity<Result> resetPassword(SetPasswordParam setPasswordParam) {
        if(!setPasswordParam.isLegal()){
            return ResponseEntity.badRequest()
                    .body(Result.fail(GlobalCodeEnum.PARAMS_ERROR.getCode(), GlobalCodeEnum.PARAMS_ERROR.getMsg()));
        }

        return ResponseEntity.ok(authService.setPassword(setPasswordParam.getEmail(), setPasswordParam.getPassword(), setPasswordParam.getVerify()));
    }


    @PostMapping("/logout")
    public ResponseEntity<Result> logout(@RequestHeader("token") String token) {
        return ResponseEntity.ok(authService.logout(token));
    }

}
