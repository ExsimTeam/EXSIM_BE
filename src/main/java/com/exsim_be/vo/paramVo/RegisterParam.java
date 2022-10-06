package com.exsim_be.vo.paramVo;

import com.exsim_be.utils.FormatCheck;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-05
 */
@Data
public class RegisterParam {
    private String username;
    private String email;
    private String password;
    private String verify;

    public boolean isLegal(){
        return FormatCheck.checkUsername(username)
                &&FormatCheck.checkPassword(password)
                &&(!StringUtils.isBlank(verify))
                && FormatCheck.checkMail(email);
    }

}
