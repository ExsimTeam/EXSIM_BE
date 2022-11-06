package com.exsim_be.controller;


import com.exsim_be.dao.FileBodyDao;
import com.exsim_be.entity.File;
import com.exsim_be.entity.FilePermission;
import com.exsim_be.entity.User;
import com.exsim_be.service.FileService;
import com.exsim_be.utils.UserThreadLocal;
import com.exsim_be.vo.FilePermissionVo;
import com.exsim_be.vo.paramVo.NewFileParam;
import com.exsim_be.vo.paramVo.ShareFileParam;
import com.exsim_be.vo.returnVo.*;
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

    @Autowired
    FileBodyDao fileBodyDao;

    private static final String uTokenPrefix="UTOKEN:";

    @GetMapping("/getFileList")
    public ResponseEntity<ResponseResult> getFileList(){
        User user= UserThreadLocal.get();
        FileListVo fileList = fileService.getFileList(user.getId());
        return ResponseEntity.ok(ResponseResult.succ(fileList));
    }



    @PostMapping("/newFile")
    public ResponseEntity<ResponseResult> newFile(NewFileParam newFileParam){
        if(!newFileParam.isLegal()){
            return ResponseEntity.badRequest().body(null);
        }
        long fileId=fileService.addNewFile(newFileParam);
        return ResponseEntity.ok(ResponseResult.succ(new NewFileRetVo(fileId)));
    }


    @PostMapping("/deleteFile")
    public ResponseEntity<ResponseResult> deleteFile(Long fileId){
        if(fileId==null){
            return ResponseEntity.badRequest().body(null);
        }
        fileService.deleteFile(fileId);
        return ResponseEntity.ok(ResponseResult.succ(null));
    }

    @PostMapping("/shareFile")
    public ResponseEntity<ResponseResult> shareFile(ShareFileParam shareFileParam){
        if(!shareFileParam.isLegal()){
            return ResponseEntity.badRequest().body(null);
        }
        //check authorization
        File file= fileService.getFile(shareFileParam.getFileId());
        if(file==null){
            return ResponseEntity.ok(ResponseResult.fail(100,"file doesn't exit!"));
        }
        User user=UserThreadLocal.get();
        if(!user.getId().equals(file.getCreateAuthorId())){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }
        //share file
        return ResponseEntity.ok(fileService
                .shareFile(shareFileParam.getShareToEmail(),shareFileParam.getFileId(),shareFileParam.getPermission()));
    }

    @GetMapping("/openFile")
    ResponseEntity<ResponseResult> openFile(@RequestParam("fileId") Long fileId){
        if(fileId==null){
            return ResponseEntity.badRequest().body(null);
        }
        User user=UserThreadLocal.get();
        File file=fileService.getFile(fileId);
        if(file==null){
            return ResponseEntity.ok(ResponseResult.fail(100,"file doesn't exit!"));
        }
        //check authorization
        FilePermission filePermission = fileService.getPermisson(user.getId(), fileId);
        FilePermissionVo filePermissionVo;
        if(filePermission==null){
            if(file.getProperty()==0){
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
            }
            filePermissionVo =new FilePermissionVo(user.getId(),user.getUsername(),fileId,0);
        }else {
            filePermissionVo =new FilePermissionVo(user.getId(), user.getUsername(),fileId, filePermission.getPermission());
        }
        String utoken=fileService.openFile(filePermissionVo);
        FileInfoVo fileInfoVo=fileBodyDao.getFileInfo(fileId);
        return ResponseEntity.ok(ResponseResult.succ(new openFileRetVo(utoken,fileInfoVo)));
    }


    @GetMapping("/getFileBody")
    ResponseEntity<ResponseResult> getFileBody(@RequestParam("fileId")Long fileId
            , @RequestParam("page") Integer page
    , @RequestParam("sheetId")int sheetId){
        if(fileId==null||page==null){
            return ResponseEntity.badRequest().body(null);
        }
        //check authorization
        GetFileBodyRetVo fileBody = fileService.getFileBody(fileId, sheetId, page);
        return ResponseEntity.ok(ResponseResult.succ(fileBody));
    }


}
