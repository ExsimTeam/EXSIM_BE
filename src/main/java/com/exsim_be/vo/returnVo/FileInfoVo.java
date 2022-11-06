package com.exsim_be.vo.returnVo;



import lombok.Data;

import java.util.Map;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-26
 */
@Data
public class FileInfoVo {
    private  int info=1;
    private int sheetNum=1;
    private Map<Integer,String> sheets;
    private int sheetPtr=1;

    public void addSheet(String sheetName){
        sheets.put(sheetPtr,sheetName);
        sheetNum++;
        sheetPtr++;
    }

    public void deleteSheet(int sheetId){
        sheets.remove(sheetId);
        sheetNum--;
    }
}
