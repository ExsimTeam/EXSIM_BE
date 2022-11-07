package com.exsim_be.service;

import com.exsim_be.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (User)表服务接口
 *
 * @author makejava
 * @since 2022-10-04 16:40:28
 */
public interface UserService {


    void updateUsername(User user);

    User getUserByEmail(String shareToEmail);

    String getUsernameById(long userId);

}
