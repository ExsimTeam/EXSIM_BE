package com.exsim_be.service;

import com.exsim_be.entity.File;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

/**
 * (File)表服务接口
 *
 * @author makejava
 * @since 2022-10-04 16:40:26
 */
public interface FileService {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    File queryById(Long id);

    /**
     * 分页查询
     *
     * @param file        筛选条件
     * @param pageRequest 分页对象
     * @return 查询结果
     */
    Page<File> queryByPage(File file, PageRequest pageRequest);

    /**
     * 新增数据
     *
     * @param file 实例对象
     * @return 实例对象
     */
    File insert(File file);


    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 是否成功
     */
    boolean deleteById(Long id);

}
