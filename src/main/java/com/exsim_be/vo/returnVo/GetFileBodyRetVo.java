package com.exsim_be.vo.returnVo;

import lombok.Data;


import java.util.List;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-25
 */
@Data
public class GetFileBodyRetVo {
    List<Object> rows;

    public GetFileBodyRetVo(List<Object> rows) {
        this.rows = rows;
    }

    public GetFileBodyRetVo() {
    }
}
