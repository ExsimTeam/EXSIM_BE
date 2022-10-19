package com.exsim_be.controller;

import com.exsim_be.entity.User;
import com.exsim_be.service.UserService;
import com.exsim_be.utils.FormatCheck;
import com.exsim_be.utils.UserThreadLocal;
import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import com.exsim_be.vo.returnVo.Result;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */

@RestController
@RequestMapping("/api/user")
public class UserController {
    @Autowired
    UserService userService;

    @PostMapping("/updateProfile")
    public ResponseEntity<Result> updateProfile(String newUsername){
        if(FormatCheck.checkUsername(newUsername)){
            return ResponseEntity.badRequest()
                    .body(Result.fail(GlobalCodeEnum.PARAMS_ERROR.getCode(),GlobalCodeEnum.PARAMS_ERROR.getMsg()));
        }
        User user= UserThreadLocal.get();
        user.setUsername(newUsername);
        userService.updateUsername(user);
        return ResponseEntity.ok(Result.succ(null));
    }
}
