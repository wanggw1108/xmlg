package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.Join;

import java.util.List;

public interface JoinMapper {
    int deleteByPrimaryKey(Long id);

    int insert(Join record);

    int insertSelective(Join record);

    Join selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Join record);

    int updateByPrimaryKey(Join record);

	Long countByParam(Join join);

	List<Join> findDataByParam(Join joinParam);
}