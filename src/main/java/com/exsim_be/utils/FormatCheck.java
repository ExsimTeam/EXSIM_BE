package com.exsim_be.utils;

import org.apache.commons.lang3.StringUtils;

import java.util.regex.Pattern;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-05
 */
public class FormatCheck {
    private static final String REG_PASSWORD = "^(?=.*\\d)(?=.*[a-zA-Z])(?=.*[!?_\\-$&+]).{8,20}$";

    private static final String REG_MAIL = "^[a-zA-Z0-9_!#$%&’*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$";

    private static final String REG_USERNAME="/^[a-zA-Z0-9_\\u4e00-\\u9fa5]{1,20}$/";

    public static boolean checkMail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        return Pattern.matches(REG_MAIL, email);
    }

    public static boolean checkUsername(String username) {
        if(StringUtils.isBlank(username)||username.length()>20){
            return false;
        }
        return username.matches(REG_USERNAME);
    }

    public static boolean checkPassword(String password) {
        if (StringUtils.isBlank(password)
                || password.length() < 8
                || password.length() > 20) {
            return false;
        }
        return password.matches(REG_PASSWORD);
    }


}
