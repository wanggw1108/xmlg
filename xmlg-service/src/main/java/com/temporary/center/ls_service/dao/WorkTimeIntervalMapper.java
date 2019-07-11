package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.WorkTimeInterval;

public interface WorkTimeIntervalMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WorkTimeInterval record);

    int insertSelective(WorkTimeInterval record);

    WorkTimeInterval selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkTimeInterval record);

    int updateByPrimaryKey(WorkTimeInterval record);
}