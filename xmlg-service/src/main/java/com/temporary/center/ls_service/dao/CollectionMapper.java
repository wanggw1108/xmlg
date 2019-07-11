package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.MyCollection;

import java.util.List;

public interface CollectionMapper {
    int deleteByPrimaryKey(Long id);

    int insert(MyCollection record);

    int insertSelective(MyCollection record);

    MyCollection selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(MyCollection record);

    int updateByPrimaryKey(MyCollection record);

	List<MyCollection> findByParam(MyCollection myCollection);

	Long countByParam(MyCollection myCollection);
}