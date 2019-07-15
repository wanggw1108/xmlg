package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.CurriculumVitaeMapper;
import com.temporary.center.ls_service.dao.EducationExperienceMapper;
import com.temporary.center.ls_service.dao.ProjectExperienceMapper;
import com.temporary.center.ls_service.dao.WorkExperienceMapper;
import com.temporary.center.ls_service.domain.CurriculumVitae;
import com.temporary.center.ls_service.domain.EducationExperience;
import com.temporary.center.ls_service.params.CurriculumParam;
import com.temporary.center.ls_service.service.CurriculumService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CurriculumServiceImpl  implements CurriculumService{
	private Logger logger = LoggerFactory.getLogger(CurriculumServiceImpl.class);

	@Autowired
	private CurriculumVitaeMapper curriculumDao;
	@Autowired
	private EducationExperienceMapper educationDao;
	@Autowired
	private ProjectExperienceMapper projectDao;
	@Autowired
	private WorkExperienceMapper workDao;

	public boolean addCurriculumParam(CurriculumParam curriculumParam,int user_id){

		try{
			CurriculumVitae vitae = curriculumParam.getCurriculum();
			vitae.setCreateby(Long.valueOf(user_id));
			curriculumDao.insert(vitae);




		}catch (Exception e){
			logger.info("创建简历失败",e);
			return false;
		}



		return true;
	}

	@Override
	public List<CurriculumVitae> list(CurriculumVitae curriculumVitae) {
		return curriculumDao.list(curriculumVitae);
	}

	@Override
	public void add(CurriculumVitae businessLicense) {
		curriculumDao.insertSelective(businessLicense);
	}
	
	
	
}
