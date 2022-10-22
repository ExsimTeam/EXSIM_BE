package com.exsim_be.vo.returnVo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-22
 */
@Data
public class openFileRetVo {
    private String utoken;

    public openFileRetVo(String utoken) {
        this.utoken = utoken;
    }

    public openFileRetVo() {
    }
}
