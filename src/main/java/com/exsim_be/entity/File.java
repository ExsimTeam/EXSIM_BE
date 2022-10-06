package com.exsim_be.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;

import java.util.Date;
import java.io.Serializable;

/**
 * (File)实体类
 *
 * @author makejava
 * @since 2022-10-04 16:40:21
 */
public class File implements Serializable {
    private static final long serialVersionUID = -58979835027250140L;

    @TableId(value ="id",type = IdType.AUTO)
    private Long id;

    private String fileName;

    private String filePath;

    private Date createdTime;

    private Date lastModifyTime;

    private Long lastModifyUserId;

    private Long createAuthorId;

    private String description;
    /**
     * 0 is private,1 is public
     */
    private Integer property;

    public File() {
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFilePath() {
        return filePath;
    }

    public void setFilePath(String filePath) {
        this.filePath = filePath;
    }

    public Date getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Date createdTime) {
        this.createdTime = createdTime;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    public Long getLastModifyUserId() {
        return lastModifyUserId;
    }

    public void setLastModifyUserId(Long lastModifyUserId) {
        this.lastModifyUserId = lastModifyUserId;
    }

    public Long getCreateAuthorId() {
        return createAuthorId;
    }

    public void setCreateAuthorId(Long createAuthorId) {
        this.createAuthorId = createAuthorId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getProperty() {
        return property;
    }

    public void setProperty(Integer property) {
        this.property = property;
    }

}

