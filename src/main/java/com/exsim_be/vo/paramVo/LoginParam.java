package com.exsim_be.vo.paramVo;

import com.exsim_be.utils.FormatCheck;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-09-16
 */
@Data
public class LoginParam {

    private String email;

    private String password;

    public boolean isLegal(){
        return (!StringUtils.isBlank(password))&& FormatCheck.checkMail(email);
    }




}
