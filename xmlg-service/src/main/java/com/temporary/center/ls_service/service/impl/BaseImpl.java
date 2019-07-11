package com.temporary.center.ls_service.service.impl;


import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import tk.mybatis.mapper.common.Mapper;
import tk.mybatis.mapper.entity.Example;

import java.util.List;


public abstract class BaseImpl<T>{

     @Autowired
     private Mapper<T> mapper;
     

     
     /**
      * 根据id查询数据
      * 
      * @param id
      * @return
      */
     public T queryById(Integer id) {
         return this.mapper.selectByPrimaryKey(id);
     }
     
     
    /**
     * 根据id查询数据
     * 
     * @param id
     * @return
     */
    public T queryById(Long id) {
        return this.mapper.selectByPrimaryKey(id);
    }

    /**
     * 查询所有数据
     * 
     * @return
     */
    public List<T> queryAll() {
        return this.mapper.select(null);
    }
    
    /**
     * 查询所有数据
     * 
     * @return
     */
    public List<T> queryAll(String redisKey, Class<T> targetClass, int timeout) 
    {

		List<T> list = this.mapper.select(null);

		if (list != null)
		{
			return list;
		}

		return null;
    }

    /**
     * 根据条件查询一条数据，如果该条件所查询的数据为多条会抛出异常
     * 
     * @param record
     * @return
     */
    public T queryOne(T record) {
        return this.mapper.selectOne(record);
    }

    /**
     * 根据条件查询多条数据
     * 
     * @param record
     * @return
     */
    public List<T> queryListByWhere(T record) {
        return this.mapper.select(record);
    }

    /**
     * 根据条件分页查询数据
     * 
     * @param record
     * @param page
     * @param rows
     * @return
     */
    public PageInfo<T> queryPageListByWhere(T record, Integer page, Integer rows) {
        // 设置分页参数
        PageHelper.startPage(page, rows);
        List<T> list = this.mapper.select(record);
        return new PageInfo<T>(list);
    }

    /**
     * 新增数据
     * 
     * @param t
     * @return
     */
    public int save(T t) {
       
        return this.mapper.insert(t);
    }

    /**
     * 选择不为null的字段作为插入数据的字段来插入数据
     * 
     * @param t
     * @return
     */
    public int saveSelective(T t) {
        return this.mapper.insertSelective(t);
    }

    /**
     * 更新数据
     * 
     * @param t
     * @return
     */
    public int update(T t) {
        return this.mapper.updateByPrimaryKey(t);
    }

    /**
     * 选择不为null的字段作为更新的字段来更新数据
     * 
     * @param t
     * @return
     */
    public int updateSelective(T t) {
        return this.mapper.updateByPrimaryKeySelective(t);
    }
    
    /**
     * 根据主键id删除数据（物理删除）
     * 
     * @param id
     * @return
     */
    public int deleteById(Integer id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 根据主键id删除数据（物理删除）
     * 
     * @param id
     * @return
     */
    public int deleteById(Long id) {
        return this.mapper.deleteByPrimaryKey(id);
    }

    /**
     * 批量删除数据
     * 
     * @param ids
     * @param clazz
     * @param property
     * @return
     */
    public int deleteByIds(List<Object> ids, Class<T> clazz, String property) {
        Example example = new Example(clazz);
        // 设置条件
        example.createCriteria().andIn(property, ids);
        return this.mapper.deleteByExample(example);
    }

    /**
     * 根据条件删除数据
     * 
     * @param record
     * @return
     */
    public int deleteByWhere(T record) {
        return this.mapper.delete(record);
    }
}
