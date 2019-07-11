package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.ProjectExperience;

public interface ProjectExperienceMapper {
    int insert(ProjectExperience record);

    int insertSelective(ProjectExperience record);
}