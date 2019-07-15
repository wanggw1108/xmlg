package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.FullTimeMapper;
import com.temporary.center.ls_service.domain.FullTime;
import com.temporary.center.ls_service.service.FullTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FullTimeServiceImpl implements FullTimeService{

	@Autowired
	private FullTimeMapper fullTimeMapper;
	
	@Override
	public List<FullTime> list(FullTime recruitmentInfo) {
		return fullTimeMapper.select(recruitmentInfo);
	}

	@Override
	public Long countByParams(FullTime recruitmentInfo) {
		return Long.valueOf(fullTimeMapper.selectCount(recruitmentInfo));
	}

	@Override
	public void add(FullTime recruitmentInfo) {
		fullTimeMapper.insertSelective(recruitmentInfo);
	}

}
