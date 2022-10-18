package com.exsim_be.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.exsim_be.dao.FileDao;
import com.exsim_be.entity.File;
import com.exsim_be.service.FileService;

import com.exsim_be.vo.returnVo.FileRetVo;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

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


    @Override
    public Page<FileRetVo> getFileList(long id,int pageNum) {
        String redisKey="FILELIST:"+id+":"+pageNum;
        String fileListJson=redisTemplate.opsForValue().get(redisKey);
        if(fileListJson!=null){
            return  JSON.parseObject(fileListJson,new TypeReference<Page<FileRetVo>>(FileRetVo.class){});
        }else {
            //一页20个
            Page<FileRetVo> fileListPage=new Page<>(pageNum,2);
            fileListPage=fileListPage.setRecords(fileDao.getFileListPage(fileListPage));
            redisTemplate.opsForValue().set(redisKey,JSON.toJSONString(fileListPage));
            return fileListPage;
        }
    }
}
