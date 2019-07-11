package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.Logtable;

public interface LogtableMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Logtable record);

    int insertSelective(Logtable record);

    Logtable selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Logtable record);

    int updateByPrimaryKey(Logtable record);
}