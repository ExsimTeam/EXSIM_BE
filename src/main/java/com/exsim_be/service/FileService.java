package com.exsim_be.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exsim_be.entity.File;
import com.exsim_be.vo.paramVo.NewFileParam;
import com.exsim_be.vo.returnVo.FileListVo;
import com.exsim_be.vo.returnVo.FileRetVo;
import com.exsim_be.vo.returnVo.Result;

/**
 * (File)表服务接口
 *
 * @author makejava
 * @since 2022-10-04 16:40:26
 */
public interface FileService extends IService<File> {


    FileListVo getFileList(long id, int page);

    void addNewFile(NewFileParam newFileParam);

    void deleteFile(long fileId);

    File getFile(Long fileId);

    Result shareFile(String shareToEmail, long fileId, int permission);
}
