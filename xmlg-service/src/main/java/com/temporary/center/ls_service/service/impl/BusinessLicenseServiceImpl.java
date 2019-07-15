package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.BusinessLicenseMapper;
import com.temporary.center.ls_service.domain.BusinessLicense;
import com.temporary.center.ls_service.service.BusinessLicenseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BusinessLicenseServiceImpl  {

	@Autowired
	private BusinessLicenseMapper businessLicenseMapper;

	public List<BusinessLicense> list(BusinessLicense businessLicense) {
		return businessLicenseMapper.select(businessLicense);
	}

	public void add(BusinessLicense businessLicense) {
		businessLicenseMapper.insertSelective(businessLicense);
	}
	
	
	
}
