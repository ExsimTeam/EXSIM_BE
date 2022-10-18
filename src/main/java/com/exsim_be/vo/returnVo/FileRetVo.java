package com.exsim_be.vo.returnVo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.exsim_be.entity.User;
import lombok.Data;

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


    private Date createdTime;

    private Date lastModifyTime;

    @TableField(value ="create_author_id" )
    private Long authorUserId;

    private String authorUsername;

    /**
     * 0 is private,1 is public
     */
    private Integer property;
}
