package com.exsim_be.vo.returnVo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-05
 */
@Data
public class LoginRetVo {
    private String token;
    private String username;

    public LoginRetVo(String token, String username) {
        this.token = token;
        this.username = username;
    }

    public LoginRetVo() {
    }
}
