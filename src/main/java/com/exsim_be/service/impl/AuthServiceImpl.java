package com.exsim_be.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exsim_be.dao.UserDao;
import com.exsim_be.entity.User;
import com.exsim_be.service.AuthService;
import com.exsim_be.service.MailService;
import com.exsim_be.utils.JWTUtils;
import com.exsim_be.vo.returnVo.LoginRetVo;
import com.exsim_be.vo.returnVo.Result;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Random;
import java.util.concurrent.TimeUnit;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-06
 */
@Service
public class AuthServiceImpl implements AuthService {
    @Autowired
    UserDao userDao;

    @Autowired
    MailService mailService;
    @Autowired
    RedisTemplate<String,String> redisTemplate;

    private static final String tokenPrefix="TOKEN:";
    private static final String userTokenPrefix="USERTOKEN:";
    private static final String verifyPrefix="VERIFY:";

    private static final String salt="erkghierkgb";
    private static final Random random=new Random();
    @Override
    public Result login(String email, String password) {
        //reCaptcha
        //密码加密
        password= DigestUtils.md5Hex(password+salt);
        //根据Email和passwor查询用户
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email).eq(User::getPassword,password)
                .select(User::getId,User::getEmail,User::getUsername);
        User user=userDao.selectOne(queryWrapper);
        //null
        if(user==null){
            return Result.fail(100,"username or password incorrect");
        }
        //单点登录，如果redis中有ID对应的token则删除旧token
        String token= JWTUtils.createToken(user.getId());
        String redisToken=tokenPrefix+token;//存放在redis中的token
        String oldToken=redisTemplate.opsForValue().get(userTokenPrefix+user.getId());
        if(oldToken!=null){
            redisTemplate.delete(oldToken);
        }
        redisTemplate.opsForValue().set(userTokenPrefix+user.getId(),redisToken,1,TimeUnit.DAYS);
        redisTemplate.opsForValue().set(redisToken, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.succ(new LoginRetVo(token,user.getUsername()));
    }

    @Override
    public void sendVerify(String email) {
        //reCaptcha
        //send mail
        String verifyCode=String.format("%06d",random.nextInt(1000000));
        redisTemplate.opsForValue().set(verifyPrefix+email,verifyCode,10,TimeUnit.MINUTES);
        mailService.sendMail(email,verifyCode);
    }

    @Override
    public Result setPassword(String email, String newPassword, String verificationCode) {
        //查询用户是否存在
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email)
                .select(User::getId);
        User user=userDao.selectOne(queryWrapper);
        if(user==null){
            return Result.fail(101,"no user");
        }
        String redisVerify=redisTemplate.opsForValue().get(verifyPrefix+email);
        if(!verificationCode.equals(redisVerify)){
            return Result.fail(100,"wrong verification code");
        }
        //update password
        newPassword=DigestUtils.md5Hex(newPassword+salt);
        user.setPassword(newPassword);
        userDao.updateById(user);
        return Result.succ(null);
    }

    @Override
    public Result logout(String token) {
        redisTemplate.delete(tokenPrefix+token);
        return Result.succ(null);
    }

    @Override
    public Result register(String username, String email, String password, String verify) {
        LambdaQueryWrapper<User> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail,email).select(User::getId);
        User user=userDao.selectOne(queryWrapper);
        //has registered
        if(user!=null){
            return Result.fail(100,"user has already registered");
        }
        //check verification code
        String redisVerify=redisTemplate.opsForValue().get(verifyPrefix+email);//redisVerify可能为null
        if(!verify.equals(redisVerify)){
            return Result.fail(101,"wrong verification code");
        }
        //add new user
        String dbPassword=DigestUtils.md5Hex(password+salt);
        user=new User();
        user.setEmail(email);
        user.setUsername(username);
        user.setPassword(dbPassword);
        userDao.insert(user);
        //login
        String token= JWTUtils.createToken(user.getId());
        String redisToken=tokenPrefix+token;//存放在redis中的token
        redisTemplate.opsForValue().set(userTokenPrefix+user.getId(),redisToken,1,TimeUnit.DAYS);
        redisTemplate.opsForValue().set(redisToken, JSON.toJSONString(user),1, TimeUnit.DAYS);
        return Result.succ(new LoginRetVo(token,user.getUsername()));
    }

    @Override
    public User checkToken(String token) {
        Map<String, Object> stringObjectMap = JWTUtils.checkToken(token);
        if (stringObjectMap == null){
            return null;
        }
        String userJson = redisTemplate.opsForValue().get(tokenPrefix + token);
        if (StringUtils.isBlank(userJson)){
            return null;
        }
        return JSON.parseObject(userJson,User.class);
    }
}
