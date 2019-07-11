package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.CompanyInfo;

import java.util.List;

public interface CompanyInfoMapper {
    int deleteByPrimaryKey(Long companyId);

    int insert(CompanyInfo record);

    int insertSelective(CompanyInfo record);

    CompanyInfo selectByPrimaryKey(Long companyId);

    int updateByPrimaryKeySelective(CompanyInfo record);

    int updateByPrimaryKey(CompanyInfo record);

	List<CompanyInfo> findByParam(CompanyInfo companyInfo);
	
	/**
	 * 查询公司简介
	 * @param companyInfo
	 * @return
	 */
	List<CompanyInfo> findByParamCompany(CompanyInfo companyInfo);

	/**
	 * 查询公司资质，主要是营业执照，其他。
	 * @param param
	 * @return
	 */
	CompanyInfo qualifications(CompanyInfo param);

	
}