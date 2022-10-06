package com.exsim_be.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.exsim_be.entity.File;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * (File)表数据库访问层
 *
 * @author makejava
 * @since 2022-10-04 16:40:20
 */
@Mapper
public interface FileDao extends BaseMapper<File> {

    /**
     * 通过ID查询单条数据
     *
     * @param id 主键
     * @return 实例对象
     */
    File queryById(Long id);

    /**
     * 查询指定行数据
     *
     * @param file     查询条件
     * @param pageable 分页对象
     * @return 对象列表
     */
    List<File> queryAllByLimit(File file, @Param("pageable") Pageable pageable);

    /**
     * 统计总行数
     *
     * @param file 查询条件
     * @return 总行数
     */
    long count(File file);

    /**
     * 新增数据
     *
     * @param file 实例对象
     * @return 影响行数
     */
    int insert(File file);

    /**
     * 批量新增数据（MyBatis原生foreach方法）
     *
     * @param entities List<File> 实例对象列表
     * @return 影响行数
     */
    int insertBatch(@Param("entities") List<File> entities);

    /**
     * 批量新增或按主键更新数据（MyBatis原生foreach方法）
     *
     * @param entities List<File> 实例对象列表
     * @return 影响行数
     * @throws org.springframework.jdbc.BadSqlGrammarException 入参是空List的时候会抛SQL语句错误的异常，请自行校验入参
     */
    int insertOrUpdateBatch(@Param("entities") List<File> entities);

    /**
     * 修改数据
     *
     * @param file 实例对象
     * @return 影响行数
     */
    int update(File file);

    /**
     * 通过主键删除数据
     *
     * @param id 主键
     * @return 影响行数
     */
    int deleteById(Long id);

}

