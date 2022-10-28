package com.exsim_be.vo.returnVo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-21
 */
@Data
public class NewFileRetVo {
    private long fileId;

    public NewFileRetVo(long fileId) {
        this.fileId = fileId;
    }

    public NewFileRetVo() {

    }
}
