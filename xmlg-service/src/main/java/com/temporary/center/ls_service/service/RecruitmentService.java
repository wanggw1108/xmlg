package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.RecruitmentInfo;

import java.util.List;

public interface RecruitmentService {

	List<RecruitmentInfo> list(RecruitmentInfo recruitmentInfo);

	Long countByParams(RecruitmentInfo recruitmentInfo);

	void add(RecruitmentInfo recruitmentInfo);

	/**
	 * 首页查询部分字段
	 * @param recruitmentInfo
	 * @return
	 */
	List<RecruitmentInfo> listIndex(RecruitmentInfo recruitmentInfo);
	
	RecruitmentInfo findById(Long parseInt);

	void update(RecruitmentInfo recruitmentInfo);

	List<String> getEmployeeByUserId(Integer user_id);

	List<String> getServiceArea(Integer user_id);
	
}
