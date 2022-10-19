package com.exsim_be.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exsim_be.dao.UserDao;
import com.exsim_be.entity.User;
import com.exsim_be.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-19
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserDao userDao;
    @Override
    public void updateUsername(User user) {
        userDao.update(user);
    }

    @Override
    public User getUserByEmail(String shareToEmail) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(User::getId,User::getEmail,User::getUsername)
                .eq(User::getEmail,shareToEmail).last("limit 1");
        return userDao.selectOne(queryWrapper);
    }
}
