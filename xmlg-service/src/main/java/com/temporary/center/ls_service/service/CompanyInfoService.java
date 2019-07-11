package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.CompanyInfo;

import java.util.List;

public interface CompanyInfoService {

	List<CompanyInfo> findByParam(CompanyInfo companyInfo);

	void add(CompanyInfo companyInfo);

	void update(CompanyInfo companyInfo);

	CompanyInfo qualifications(CompanyInfo param);

}
