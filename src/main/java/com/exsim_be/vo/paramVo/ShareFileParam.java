package com.exsim_be.vo.paramVo;

import com.exsim_be.utils.FormatCheck;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-19
 */
@Data
public class ShareFileParam {
    private String shareToEmail;
    private Integer permission;

    private Long fileId;
    public boolean isLegal(){
        return FormatCheck.checkMail(shareToEmail) &&permission!=null&&fileId!=null&&(permission==0||permission==1);
    }
}
