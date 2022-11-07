package com.exsim_be.vo.websocketVo;


import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import lombok.Data;



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

    private String sender;

    public MessageVo(int code, String msg, int opcode, Object data,String sender) {
        this.code = code;
        this.msg = msg;
        this.opcode = opcode;
        this.data = data;
        this.sender=sender;

    }

    public MessageVo() {
    }


    public static MessageVo succ(int opcode,Object data,String sender){
        return new MessageVo(GlobalCodeEnum.SUCCESS.getCode(), GlobalCodeEnum.SUCCESS.getMsg(), opcode,data,sender);
    }

    public static MessageVo fail(int code,String msg){
        return new MessageVo(code,msg,-1,null,null);
    }
}
