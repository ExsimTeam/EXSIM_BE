package com.exsim_be.vo.websocketVo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-25
 */
@Data
public class CellVo {
    private Integer sheetId;
    private Long row;
    private Long col;
    private String value;
    private String format;
 }
