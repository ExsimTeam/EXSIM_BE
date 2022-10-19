package com.exsim_be.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exsim_be.dao.FileDao;
import com.exsim_be.dao.FilePermissionDao;
import com.exsim_be.entity.File;
import com.exsim_be.entity.User;
import com.exsim_be.service.FilePermissionService;
import com.exsim_be.service.FileService;

import com.exsim_be.service.UserService;
import com.exsim_be.utils.UserThreadLocal;
import com.exsim_be.vo.paramVo.NewFileParam;
import com.exsim_be.vo.returnVo.FileListVo;
import com.exsim_be.vo.returnVo.FileRetVo;
import com.exsim_be.vo.returnVo.Result;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-11
 */
@Service
public class FileServiceImpl extends ServiceImpl<FileDao, File> implements FileService {
    @Autowired
    RedisTemplate<String,String> redisTemplate;
    @Autowired
    FileDao fileDao;
    @Autowired
    FilePermissionService filePermissionService;

    @Autowired
    UserService userService;

    @Override
    public FileListVo getFileList(long id, int pageNum) {
        //一页20个
        Page<FileRetVo> fileListPage=new Page<>(pageNum,20);
        fileListPage=fileListPage.setRecords(fileDao.getFileListPage(fileListPage));
        //copy
        FileListVo fileListVo=new FileListVo();
        fileListVo.setFiles(fileListPage.getRecords());
        fileListVo.setCurrent(fileListPage.getCurrent());
        fileListVo.setSize(fileListPage.getSize());
        fileListVo.setTotal(fileListPage.getTotal());
        return fileListVo;
    }

    @Override
    public void addNewFile(NewFileParam newFileParam) {
        User user= UserThreadLocal.get();
        File newFile=new File();
        newFile.setFileName(newFileParam.getFileName());
        newFile.setProperty(newFileParam.getProperty());
        newFile.setDescription(newFileParam.getDescription());
        newFile.setCreatedTime(new Date());
        newFile.setLastModifyTime(new Date());
        newFile.setCreateAuthorId(user.getId());
        newFile.setLastModifyUserId(user.getId());
        fileDao.insert(newFile);
        filePermissionService.addPermission(user.getId(),newFile.getId(),1);
        //mongodb add new collection
    }

    @Override
    public void deleteFile(long fileId) {
        User user=UserThreadLocal.get();
        File file=fileDao.queryById(fileId);
        if(file!=null&&file.getCreateAuthorId().equals(user.getId())){
            fileDao.deleteById(fileId);
            filePermissionService.deleteBatchByFileId(fileId);
            //delete file in mongodb and redis
        }else {
            filePermissionService.delete(user.getId(), fileId);
        }
    }

    @Override
    public File getFile(Long fileId) {
        return fileDao.queryById(fileId);
    }

    @Override
    public Result shareFile(String shareToEmail, long fileId,int permission) {
        User shareToUser=userService.getUserByEmail(shareToEmail);
        if(shareToUser==null){
            return Result.fail(101,"user doesn't exist!");
        }
        filePermissionService.addPermission(shareToUser.getId(),fileId,permission);
        return Result.succ(shareToUser.getUsername());
    }
}
