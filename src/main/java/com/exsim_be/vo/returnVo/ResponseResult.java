package com.exsim_be.vo.returnVo;

import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
@Data
public class ResponseResult {


    private int code;

    private String msg;

    private Object result;

    public ResponseResult(int code, String msg, Object result) {
        this.code = code;
        this.msg = msg;
        this.result = result;
    }

    public ResponseResult() {
    }

    public static ResponseResult succ(Object data){
        return new ResponseResult(GlobalCodeEnum.SUCCESS.getCode(),GlobalCodeEnum.SUCCESS.getMsg(), data);
    }

    public static ResponseResult fail(int code, String msg){
        return new ResponseResult(code,msg,null);
    }

}