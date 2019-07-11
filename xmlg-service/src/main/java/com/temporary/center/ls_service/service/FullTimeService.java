package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.FullTime;

import java.util.List;

public interface FullTimeService {

	List<FullTime> list(FullTime fullTime);

	Long countByParams(FullTime fullTime);

	void add(FullTime fullTime);
	
}
