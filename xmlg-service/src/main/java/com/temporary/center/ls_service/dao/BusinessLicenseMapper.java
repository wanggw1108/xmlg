package com.temporary.center.ls_service.dao;


import com.temporary.center.ls_service.domain.BusinessLicense;
import com.temporary.center.ls_service.domain.Dictionaries;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;

public interface BusinessLicenseMapper extends Mapper<BusinessLicense> {
    int insert(BusinessLicense record);

    int insertSelective(BusinessLicense record);

	List<Dictionaries> list(BusinessLicense businessLicense);
	
}