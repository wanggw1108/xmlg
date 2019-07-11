package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.WorkTimeMapper;
import com.temporary.center.ls_service.domain.WorkTime;
import com.temporary.center.ls_service.service.WorkTimeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkTimeServiceImpl implements WorkTimeService {

	@Autowired
	private WorkTimeMapper workTimeMapper;
	
	@Override
	public void add(WorkTime workTime) {
		workTimeMapper.insertSelective(workTime);
	}

	
	
}
