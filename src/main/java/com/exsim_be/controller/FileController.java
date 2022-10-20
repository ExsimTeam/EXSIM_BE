package com.exsim_be.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exsim_be.entity.File;
import com.exsim_be.entity.User;
import com.exsim_be.service.FileService;
import com.exsim_be.utils.UserThreadLocal;
import com.exsim_be.vo.paramVo.NewFileParam;
import com.exsim_be.vo.paramVo.ShareFileParam;
import com.exsim_be.vo.returnVo.FileListVo;
import com.exsim_be.vo.returnVo.FileRetVo;
import com.exsim_be.vo.returnVo.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import org.springframework.web.bind.annotation.*;



/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-04
 */
@RequestMapping("/api/file")
@RestController
public class FileController {
    @Autowired
    FileService fileService;

    @GetMapping("/getFileList/{page}")
    public ResponseEntity<Result> getFileList(@PathVariable("page") Integer page){
        if(page==null){
            return ResponseEntity.badRequest().body(null);
        }
        User user= UserThreadLocal.get();
        FileListVo fileList = fileService.getFileList(user.getId(), page);
        return ResponseEntity.ok(Result.succ(fileList));
    }



    @PostMapping("/newFile")
    public ResponseEntity<Result> newFile(NewFileParam newFileParam){
        if(!newFileParam.isLegal()){
            return ResponseEntity.badRequest().body(null);
        }
        fileService.addNewFile(newFileParam);
        return ResponseEntity.ok(Result.succ(null));
    }


    @PostMapping("/deleteFile")
    public ResponseEntity<Result> deleteFile(Long fileId){
        if(fileId==null){
            return ResponseEntity.badRequest().body(null);
        }
        fileService.deleteFile(fileId);
        return ResponseEntity.ok(Result.succ(null));
    }

    @PostMapping("/shareFile")
    public ResponseEntity<Result> shareFile(ShareFileParam shareFileParam){
        if(!shareFileParam.isLegal()){
            return ResponseEntity.badRequest().body(null);
        }
        //check authorization
        File file= fileService.getFile(shareFileParam.getFileId());
        if(file==null){
            return ResponseEntity.ok(Result.fail(100,"file doesn't exit!"));
        }
        User user=UserThreadLocal.get();
        if(!user.getId().equals(file.getCreateAuthorId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        //share file
        return ResponseEntity.ok(fileService
                .shareFile(shareFileParam.getShareToEmail(),shareFileParam.getFileId(),shareFileParam.getPermission()));
    }












}
