package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.Provincial;

public interface ProvincialMapper {
    int deleteByPrimaryKey(Integer pid);

    int insert(Provincial record);

    int insertSelective(Provincial record);

    Provincial selectByPrimaryKey(Integer pid);

    int updateByPrimaryKeySelective(Provincial record);

    int updateByPrimaryKey(Provincial record);
}