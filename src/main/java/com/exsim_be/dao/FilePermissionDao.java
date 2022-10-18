package com.exsim_be.dao;

import com.exsim_be.entity.FilePermission;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (FilePermission)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-12 14:56:46
 */
public interface FilePermissionDao {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    FilePermission queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param filePermission 查询条件
     * @param pageable       分页对象
     * @return 对象列表
     */
    List<FilePermission> queryAllByLimit(FilePermission filePermission, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param filePermission 查询条件
     * @return 总行数
     */
    long count(FilePermission filePermission);

    /**
     * 新增数据
     *
     * @param filePermission 实例对象
     * @return 影响行数
     */
    int insert(FilePermission filePermission);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<FilePermission> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<FilePermission> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<FilePermission> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<FilePermission> entities);

    /**
     * 修改数据
     *
     * @param filePermission 实例对象
     * @return 影响行数
     */
    int update(FilePermission filePermission);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

