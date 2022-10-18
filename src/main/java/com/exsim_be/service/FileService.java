package com.exsim_be.service;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.exsim_be.entity.File;
import com.exsim_be.vo.returnVo.FileRetVo;

/**
 * (File)表服务接口
 *
 * @author makejava
 * @since 2022-10-04 16:40:26
 */
public interface FileService extends IService<File> {


    Page<FileRetVo> getFileList(long id, int page);
}
