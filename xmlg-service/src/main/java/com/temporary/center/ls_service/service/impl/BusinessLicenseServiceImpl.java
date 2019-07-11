package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.BusinessLicenseMapper;
import com.temporary.center.ls_service.domain.BusinessLicense;
import com.temporary.center.ls_service.domain.Dictionaries;
import com.temporary.center.ls_service.service.BusinessLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessLicenseServiceImpl implements BusinessLicenseService {

	@Autowired
	private BusinessLicenseMapper businessLicenseMapper;
	
	@Override
	public List<Dictionaries> list(BusinessLicense businessLicense) {
		return businessLicenseMapper.list(businessLicense);
	}

	@Override
	public void add(BusinessLicense businessLicense) {
		businessLicenseMapper.insertSelective(businessLicense);
	}
	
	
	
}
