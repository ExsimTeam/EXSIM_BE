package com.exsim_be.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.io.Serializable;

/**
 * (User)实体类
 *
 * @author makejava
 * @since 2022-10-04 16:40:28
 */
public class User implements Serializable {
    private static final long serialVersionUID = -90374057732151000L;

    @TableId(value ="id",type = IdType.AUTO)
    private Long id;

    private String username;

    private String password;

    private String email;
    /**
     * 0 is frozen account,1 has activiated
     */
    private Integer status;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

}

