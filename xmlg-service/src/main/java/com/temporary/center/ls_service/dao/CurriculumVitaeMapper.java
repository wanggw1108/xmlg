package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.CurriculumVitae;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface CurriculumVitaeMapper extends Mapper<CurriculumVitae> {
    int insert(CurriculumVitae record);

    int insertSelective(CurriculumVitae record);

	List<CurriculumVitae> list(CurriculumVitae curriculumVitae);
}