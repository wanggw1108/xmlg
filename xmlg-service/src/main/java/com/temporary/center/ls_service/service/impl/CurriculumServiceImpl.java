package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.CurriculumVitaeMapper;
import com.temporary.center.ls_service.domain.CurriculumVitae;
import com.temporary.center.ls_service.service.CurriculumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurriculumServiceImpl implements CurriculumService {

	@Autowired
	private CurriculumVitaeMapper dao;
	
	@Override
	public List<CurriculumVitae> list(CurriculumVitae curriculumVitae) {
		return dao.list(curriculumVitae);
	}

	@Override
	public void add(CurriculumVitae businessLicense) {
		dao.insertSelective(businessLicense);
	}
	
	
	
}
