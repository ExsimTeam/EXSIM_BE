package com.exsim_be.utils;

import com.exsim_be.entity.User;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-05
 */
public class UserThreadLocal {
    private UserThreadLocal(){}
    //线程变量隔离
    private static final ThreadLocal<User> LOCAL = new ThreadLocal<>();

    public static void put(User user){
        LOCAL.set(user);
    }

    public static User get(){
        return LOCAL.get();
    }

    public static void remove(){
        LOCAL.remove();
    }
}
