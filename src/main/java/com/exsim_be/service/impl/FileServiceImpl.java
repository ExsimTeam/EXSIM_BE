package com.exsim_be.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exsim_be.dao.FileBodyDao;
import com.exsim_be.dao.FileDao;
import com.exsim_be.entity.File;
import com.exsim_be.entity.FilePermission;
import com.exsim_be.entity.User;
import com.exsim_be.service.FilePermissionService;
import com.exsim_be.service.FileService;

import com.exsim_be.service.UserService;
import com.exsim_be.utils.UserThreadLocal;
import com.exsim_be.vo.FilePermissionVo;
import com.exsim_be.vo.paramVo.NewFileParam;
import com.exsim_be.vo.returnVo.*;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;


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
    FileBodyDao fileBodyDao;

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
    public long addNewFile(NewFileParam newFileParam) {
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
        fileBodyDao.addNewFile(newFile.getId());
        return newFile.getId();
    }

    @Override
    public void deleteFile(long fileId) {
        User user=UserThreadLocal.get();
        File file=fileDao.queryById(fileId);
        if(file!=null&&file.getCreateAuthorId().equals(user.getId())){
            fileDao.deleteById(fileId);
            filePermissionService.deleteBatchByFileId(fileId);
            //delete file in mongodb and redis
            fileBodyDao.deleteFile(fileId);
        }else {
            filePermissionService.delete(user.getId(), fileId);
        }
    }

    @Override
    public File getFile(long fileId) {
        return fileDao.queryById(fileId);
    }

    @Override
    public Result shareFile(String shareToEmail, long fileId,int permission) {
        User shareToUser=userService.getUserByEmail(shareToEmail);
        if(shareToUser==null){
            return Result.fail(101,"user doesn't exist!");
        }
        filePermissionService.addPermission(shareToUser.getId(),fileId,permission);
        return Result.succ(new ShareFileRetVo(shareToUser.getUsername()));
    }

    @Override
    public FilePermission getPermisson(long id, long fileId) {
        return filePermissionService.getPermission(id,fileId);
    }


    @Override
    public String openFile(FilePermissionVo filePermissionVo) {
        String utoken= UUID.randomUUID().toString();
        redisTemplate.opsForValue().set("UTOKEN:"+utoken, JSON.toJSONString(filePermissionVo),3, TimeUnit.MINUTES);
        return utoken;
    }

    @Override
    public GetFileBodyRetVo getFileBody(long fileId,int sheetId, int page) {
        List<Object> objects = fileBodyDao.queryCell(fileId, sheetId, page);
        return new GetFileBodyRetVo(objects);
    }
}
