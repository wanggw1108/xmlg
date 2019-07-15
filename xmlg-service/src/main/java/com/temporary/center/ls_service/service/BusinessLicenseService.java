package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.BusinessLicense;
import com.temporary.center.ls_service.domain.Dictionaries;

import java.util.List;

public interface BusinessLicenseService {

	List<BusinessLicenseService> list(BusinessLicense businessLicense);

	void add(BusinessLicense businessLicense);

}
