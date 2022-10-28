package com.exsim_be.vo.websocketVo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-27
 */
@Data
public class ReceiveMessageVo {
    private int opcode;
    private String data;
}
