package com.exsim_be.vo;

import lombok.Data;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-22
 */
@Data
public class FilePermissionVo {
    private long userId;

    private String username;

    private long fileId;
    /**
     * 0 is read only,1 is write and read
     */
    private Integer permission;

    public FilePermissionVo() {
    }


    public FilePermissionVo(long userId, String username, long fileId, Integer permission) {
        this.userId = userId;
        this.username = username;
        this.fileId = fileId;
        this.permission = permission;
    }
}
