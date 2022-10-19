package com.exsim_be.vo.paramVo;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.text.DecimalFormat;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-19
 */
@Data
public class NewFileParam {
    private String fileName;
    private String description;
    private Integer property;

    public boolean isLegal(){
        if(StringUtils.isBlank(fileName)||property==null){
            return false;
        }
        if(fileName.length()>=100||(!StringUtils.isBlank(description)&& description.length()>=200)||(property!=0&&property!=1)){
            return false;
        }
        return true;
    }
}
