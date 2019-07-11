package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.CurriculumVitae;

import java.util.List;

public interface CurriculumVitaeMapper {
    int insert(CurriculumVitae record);

    int insertSelective(CurriculumVitae record);

	List<CurriculumVitae> list(CurriculumVitae curriculumVitae);
}