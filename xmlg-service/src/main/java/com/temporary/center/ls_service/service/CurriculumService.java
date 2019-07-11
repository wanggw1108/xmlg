package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.CurriculumVitae;

import java.util.List;

public interface CurriculumService {

	List<CurriculumVitae> list(CurriculumVitae curriculumVitae);

	void add(CurriculumVitae curriculumVitae);

}
