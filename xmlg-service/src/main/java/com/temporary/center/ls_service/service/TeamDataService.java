package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.TeamData;

import java.util.List;

public interface TeamDataService {

	List<TeamData> list(TeamData businessLicense);

	void add(TeamData businessLicense);

	TeamData findDataById(Long id);

	
	
}
