package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.FullTime;

import java.util.List;

public interface FullTimeMapper {
    int insert(FullTime record);

    int insertSelective(FullTime record);

	List<FullTime> list(FullTime recruitmentInfo);

	Long countByParams(FullTime recruitmentInfo);
}