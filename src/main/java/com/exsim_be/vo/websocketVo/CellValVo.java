package com.exsim_be.vo.websocketVo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-25
 */
@Data
public class CellValVo {
    private String value;
    private String format;

    public CellValVo() {
    }

    public CellValVo(String value, String format) {
        this.value = value;
        this.format = format;
    }
}
