package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.Join;

import java.util.List;

public interface JoinService {

	void join(Join join);

	Long countByParam(Join join);

	List<Join> findDataByParam(Join joinParam);

}
