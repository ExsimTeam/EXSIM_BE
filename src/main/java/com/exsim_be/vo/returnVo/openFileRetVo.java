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
    private FileInfoVo fileInfoVo;

    public openFileRetVo(String utoken, FileInfoVo fileInfoVo) {
        this.utoken = utoken;
        this.fileInfoVo = fileInfoVo;
    }

    public openFileRetVo() {
    }
}
