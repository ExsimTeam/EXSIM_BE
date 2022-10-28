package com.exsim_be.vo.enumVo;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
public enum GlobalCodeEnum {
    SUCCESS(1,"success"),
    PARAMS_ERROR(2,"params error"),
    BAD_REQUEST(400,"bad request"),
    UNAUTHORIZED(401,"unauthorized");
    private int code;
    private String msg;

    GlobalCodeEnum(int code, String msg){
        this.code = code;
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
