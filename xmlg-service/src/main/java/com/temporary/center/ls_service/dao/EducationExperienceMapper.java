package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.EducationExperience;

public interface EducationExperienceMapper {
    int insert(EducationExperience record);

    int insertSelective(EducationExperience record);
}