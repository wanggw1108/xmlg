package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.WorkExperience;

public interface WorkExperienceMapper {
    int insert(WorkExperience record);

    int insertSelective(WorkExperience record);
}