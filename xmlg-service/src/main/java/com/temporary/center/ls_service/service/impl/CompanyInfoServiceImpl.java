package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.CompanyInfoMapper;
import com.temporary.center.ls_service.domain.CompanyInfo;
import com.temporary.center.ls_service.service.CompanyInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CompanyInfoServiceImpl implements CompanyInfoService {

	@Autowired
	private CompanyInfoMapper companyInfoMapper;

	@Override
	public List<CompanyInfo> findByParam(CompanyInfo companyInfo) {
		return companyInfoMapper.findByParamCompany(companyInfo);
	}

	@Override
	public void add(CompanyInfo companyInfo) {
		companyInfoMapper.insertSelective(companyInfo);
	}

	@Override
	public void update(CompanyInfo companyInfo) {
		companyInfoMapper.updateByPrimaryKeySelective(companyInfo);
	}

	@Override
	public CompanyInfo qualifications(CompanyInfo param) {
		return companyInfoMapper.qualifications(param);
	}
	
	
	
	
	
}
