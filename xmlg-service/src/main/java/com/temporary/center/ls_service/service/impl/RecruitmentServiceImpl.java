package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.RecruitmentInfoMapper;
import com.temporary.center.ls_service.domain.Locus;
import com.temporary.center.ls_service.domain.RecruitmentInfo;
import com.temporary.center.ls_service.service.RecruitmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RecruitmentServiceImpl implements RecruitmentService{

	@Autowired
	private RecruitmentInfoMapper recruitmentInfoMapper;
	
	/**
	 * 查询所有字段
	 */
	@Override
	public List<RecruitmentInfo> list(RecruitmentInfo recruitmentInfo) {
		return recruitmentInfoMapper.list(recruitmentInfo);
	}

	@Override
	public Long countByParams(RecruitmentInfo recruitmentInfo) {
		return recruitmentInfoMapper.countByParams(recruitmentInfo);
	}

	@Override
	public void add(RecruitmentInfo recruitmentInfo) {
		recruitmentInfoMapper.insertSelective(recruitmentInfo);
	}

	@Override
	public List<RecruitmentInfo> listIndex(RecruitmentInfo recruitmentInfo) {
		return recruitmentInfoMapper.listIndex(recruitmentInfo);
	}

	@Override
	public RecruitmentInfo findById(Long parseInt) {
		return recruitmentInfoMapper.selectByPrimaryKey(parseInt);
	}

	@Override
	public void update(RecruitmentInfo recruitmentInfo) {
		recruitmentInfoMapper.updateByPrimaryKeySelective(recruitmentInfo);
	}

	@Override
	public List<Locus> getEmployeeByUserId(Integer user_id) {
		return recruitmentInfoMapper.getEmployeeByUserId(user_id);
	}

	@Override
	public List<Locus> getServiceArea(Integer user_id) {
		return recruitmentInfoMapper.getServiceArea(user_id);
	}

}
