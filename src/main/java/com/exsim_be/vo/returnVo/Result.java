package com.exsim_be.vo.returnVo;

import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
@Data
public class Result {


    private int code;

    private String msg;

    private Object data;

    public Result(int code, String msg, Object data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result() {
    }

    public static Result succ(Object data){
        return new Result(GlobalCodeEnum.SUCCESS.getCode(),GlobalCodeEnum.SUCCESS.getMsg(), data);
    }

    public static Result fail(int code,String msg){
        return new Result(code,msg,null);
    }

}