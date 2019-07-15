package com.temporary.center.ls_service.service;

import com.github.pagehelper.PageInfo;
import com.temporary.center.ls_service.domain.QueryExample;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-15-14:19
 */
public interface BaseServices<T, ID> {

    /**
     * 保存一个实体，null的属性不会保存，会使用数据库默认值
     *
     * @param t
     * @return
     */
    Integer add(T t);

    /**
     * 保存一个list实体，null的属性不会保存，会使用数据库默认值
     *
     * @param list
     * @return
     */
    Integer batchAdd(List<T> list);

    /**
     * 根据id删除
     *
     * @param id
     * @return
     */
    Integer deleteById(ID id);

    /**
     * 根据实体属性作为条件进行删除，查询条件使用等号
     *
     * @param t
     * @return
     */
    Integer delete(T t);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param t
     * @return
     */
    Integer updateByPrimaryKey(T t);

    /**
     * 根据主键更新属性不为null的值
     *
     * @param list
     * @return
     */
    Integer batchUpdateByPrimaryKey(List<T> list);

    /**
     * 根据实体中的属性进行查询，只能有一个返回值，有多个结果是抛出异常，查询条件使用等号
     *
     * @param t
     * @return
     */
    T findOne(T t);

    /**
     * 查询全部结果
     *
     * @return
     */
    List<T> findAll();

    /**
     * 根据主键查询
     *
     * @param id
     * @return
     */
    T findById(ID id);

    /**
     * 根据实体中的属性值进行查询，查询条件使用等号
     *
     * @param t
     * @return
     */
    List<T> find(T t);

    /**
     * 根据Example条件更新实体`record`包含的不是null的属性值
     *
     * @return
     */
    Integer updateByExampleSelective(QueryExample<T> queryExample);

    /**
     * 根据实体中的属性值进行分页查询，查询条件使用等号
     *
     * @param t
     * @param pageNum
     * @param pageSize
     * @return
     */
    PageInfo<T> findPage(T t, Integer pageNum, Integer pageSize);

    List<T> findByExample(Example example);

    /**
     * 根据query条件更新record数据
     *
     * @param record 要更新的数据
     * @param query  查询条件
     * @return
     */
    Integer updateByExampleSelective(T record, Example query);

    /**
     * 根据query条件更新record数据
     *
     * @param record 要更新的数据
     * @param query  查询条件
     * @return
     */
    Integer updateByExampleSelective(T record, T query);

    /**
     * 查询数量
     *
     * @param record
     * @return
     */
    Integer findCount(T record);

    /**
     * 查询数量
     *
     * @param query
     * @return
     */
    Integer findCountByExample(Example query);

}
