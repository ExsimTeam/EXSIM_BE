package com.exsim_be.service;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-19
 */
public interface FilePermissionService {
    void addPermission(long userId,long fileId,int permission);

    void delete(long userId, long fileId);

    void deleteBatchByFileId(long fileId);

}
