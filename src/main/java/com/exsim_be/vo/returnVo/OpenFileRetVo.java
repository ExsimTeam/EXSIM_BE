package com.exsim_be.vo.returnVo;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-22
 */
@Data
public class OpenFileRetVo {
    private String utoken;
    private FileInfoVo fileInfoVo;
    private String fileName;
    private String author;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastModify;

    public OpenFileRetVo( FileInfoVo fileInfoVo) {
        this.fileInfoVo = fileInfoVo;
    }

    public OpenFileRetVo() {
    }
}
