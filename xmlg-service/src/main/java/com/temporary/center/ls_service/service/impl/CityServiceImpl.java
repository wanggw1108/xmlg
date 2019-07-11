package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.CityMapper;
import com.temporary.center.ls_service.domain.City;
import com.temporary.center.ls_service.service.CityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CityServiceImpl implements CityService {
	
	@Autowired
	private CityMapper cityMapper;
	
	@Override
	public List<City> getCityALL() {
		return cityMapper.getCityALL();
	}

}
