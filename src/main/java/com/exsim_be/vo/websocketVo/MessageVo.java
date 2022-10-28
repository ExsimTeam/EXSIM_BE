package com.exsim_be.vo.websocketVo;

import com.exsim_be.service.MailService;
import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import lombok.Data;

import java.io.PipedReader;
import java.util.Date;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-22
 */
@Data
public class MessageVo {
    private int code;
    private String msg;
    private int opcode;
    private Object data;

    public MessageVo(int code, String msg, int opcode, Object data) {
        this.code = code;
        this.msg = msg;
        this.opcode = opcode;
        this.data = data;
    }

    public MessageVo() {
    }


    public static MessageVo succ(int opcode,Object data){
        return new MessageVo(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMsg(), opcode,data);
    }

    public static MessageVo fail(int code,String msg){
        return new MessageVo(code,msg,-1,null);
    }
}
