package com.exsim_be.entity;

import java.util.Date;
import java.io.Serializable;

/**
 * (FilePermission)实体类
 *
 * @author makejava
 * @since 2022-10-12 14:56:46
 */
public class FilePermission implements Serializable {
    private static final long serialVersionUID = 372267280282481655L;

    private Long id;

    private Long userId;

    private Long fileId;
    /**
     * 0 is read only,1 is write and read
     */
    private Integer permission;

    private Date lastOpenTime;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getFileId() {
        return fileId;
    }

    public void setFileId(Long fileId) {
        this.fileId = fileId;
    }

    public Integer getPermission() {
        return permission;
    }

    public void setPermission(Integer permission) {
        this.permission = permission;
    }

    public Date getLastOpenTime() {
        return lastOpenTime;
    }

    public void setLastOpenTime(Date lastOpenTime) {
        this.lastOpenTime = lastOpenTime;
    }

}

