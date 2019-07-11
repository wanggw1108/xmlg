package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.WorkTimeIntervalMapper;
import com.temporary.center.ls_service.domain.WorkTimeInterval;
import com.temporary.center.ls_service.service.WorkTimeIntervalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkTimeIntervalServiceImpl implements WorkTimeIntervalService {

	@Autowired
	private WorkTimeIntervalMapper workTimeIntervalMapper;
	
	@Override
	public void add(WorkTimeInterval workTimeInterval) {
		workTimeIntervalMapper.insertSelective(workTimeInterval);
	}

}
