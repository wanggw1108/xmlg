package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.TeamDataMapper;
import com.temporary.center.ls_service.domain.TeamData;
import com.temporary.center.ls_service.service.TeamDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeamDataServiceImpl implements TeamDataService {

	@Autowired
	private TeamDataMapper dao;
	
	@Override
	public List<TeamData> list(TeamData businessLicense) {
		return null;
	}

	@Override
	public void add(TeamData businessLicense) {
		dao.insertSelective(businessLicense);
	}

	@Override
	public TeamData findDataById(Long id) {
		return null;
	}
	
	
	
}
