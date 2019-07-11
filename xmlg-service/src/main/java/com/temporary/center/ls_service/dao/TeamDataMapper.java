package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.TeamData;

import java.util.List;

public interface TeamDataMapper {
    int insert(TeamData record);

    int insertSelective(TeamData record);

	List<TeamData> list(TeamData businessLicense);

	TeamData findDataById(Long id);
}