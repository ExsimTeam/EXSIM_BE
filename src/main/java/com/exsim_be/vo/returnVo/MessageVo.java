package com.exsim_be.vo.returnVo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-22
 */
@Data
public class MessageVo {
    private String message;
    private String sender;

    public MessageVo(String message, String sender) {
        this.message = message;
        this.sender = sender;
    }

    public MessageVo() {
    }
}
