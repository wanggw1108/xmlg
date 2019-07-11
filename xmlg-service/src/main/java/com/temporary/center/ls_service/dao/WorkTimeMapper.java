package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.WorkTime;

public interface WorkTimeMapper {
    int deleteByPrimaryKey(Long id);

    int insert(WorkTime record);

    int insertSelective(WorkTime record);

    WorkTime selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(WorkTime record);

    int updateByPrimaryKey(WorkTime record);
}