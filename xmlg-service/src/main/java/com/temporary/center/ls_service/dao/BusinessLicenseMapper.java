package com.temporary.center.ls_service.dao;


import com.temporary.center.ls_service.domain.BusinessLicense;
import com.temporary.center.ls_service.domain.Dictionaries;
import org.springframework.stereotype.Repository;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
@Repository
public interface BusinessLicenseMapper extends Mapper<BusinessLicense> {
	
}