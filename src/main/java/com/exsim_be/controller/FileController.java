package com.exsim_be.controller;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.exsim_be.entity.User;
import com.exsim_be.service.FileService;
import com.exsim_be.utils.UserThreadLocal;
import com.exsim_be.vo.enumVo.GlobalCodeEnum;
import com.exsim_be.vo.returnVo.FileRetVo;
import com.exsim_be.vo.returnVo.Result;
import org.springframework.beans.factory.annotation.Autowired;
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
            return ResponseEntity.badRequest()
                    .body(Result.fail(GlobalCodeEnum.PARAMS_ERROR.getCode(), GlobalCodeEnum.PARAMS_ERROR.getMsg()));
        }
        User user= UserThreadLocal.get();
        Page<FileRetVo> fileList = fileService.getFileList(user.getId(), page);
        return ResponseEntity.ok(Result.succ(fileList));
    }
}
