package com.exsim_be.vo.returnVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.exsim_be.entity.User;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * @author 贾楠
 * @version 1.0
 * @date 2022-10-18
 */
@Data
public class FileRetVo implements Serializable {

    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    private String fileName;


    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createdTime;

    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(pattern ="yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date lastModifyTime;

    @TableField(value ="create_author_id" )
    private Long authorUserId;

    private String authorUsername;

    /**
     * 0 is private,1 is public
     */
    private Integer property;
}
