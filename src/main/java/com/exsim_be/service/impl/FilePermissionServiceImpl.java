package com.exsim_be.service.impl;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.exsim_be.dao.FilePermissionDao;
import com.exsim_be.entity.FilePermission;
import com.exsim_be.service.FilePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-19
 */
@Service
public class FilePermissionServiceImpl implements FilePermissionService {
    @Autowired
    FilePermissionDao filePermissionDao;
    @Override
    public void addPermission(long userId, long fileId, int permission) {
        //check whether exists
        LambdaQueryWrapper<FilePermission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.select(FilePermission::getId,FilePermission::getPermission)
                .eq(FilePermission::getFileId,fileId).eq(FilePermission::getUserId,userId)
                .last("limit 1");
        FilePermission filePermission=filePermissionDao.selectOne(queryWrapper);
        if(filePermission!=null){
            if(filePermission.getPermission()!=permission){
                filePermission.setPermission(permission);
                filePermissionDao.update(filePermission);
            }
        }else {
            filePermission=new FilePermission();
            filePermission.setFileId(fileId);
            filePermission.setUserId(userId);
            filePermission.setPermission(permission);
            filePermissionDao.insert(filePermission);
        }
    }

    @Override
    public void delete(long userId, long fileId) {
        LambdaQueryWrapper<FilePermission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(FilePermission::getUserId,userId).eq(FilePermission::getFileId,fileId);
        filePermissionDao.delete(queryWrapper);
    }

    @Override
    public void deleteBatchByFileId(long fileId) {
        LambdaQueryWrapper<FilePermission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(FilePermission::getFileId,fileId);
        filePermissionDao.delete(queryWrapper);
    }

    @Override
    public FilePermission getPermission(long userId, long FileId) {
        LambdaQueryWrapper<FilePermission> queryWrapper=new LambdaQueryWrapper<>();
        queryWrapper.eq(FilePermission::getUserId,userId).eq(FilePermission::getFileId,FileId)
                .select(FilePermission::getId, FilePermission::getPermission)
                .last("limit 1");
        FilePermission filePermission = filePermissionDao.selectOne(queryWrapper);
        return filePermission;
    }


}
