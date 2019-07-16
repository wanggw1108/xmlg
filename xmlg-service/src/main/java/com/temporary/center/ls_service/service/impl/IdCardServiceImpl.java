package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.IdCardMapper;
import com.temporary.center.ls_service.domain.IdCard;
import com.temporary.center.ls_service.service.IdCardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class IdCardServiceImpl implements IdCardService {

	@Autowired
	private IdCardMapper idCardMapper;
	
	@Override
	public List<IdCard> list(IdCard businessLicense) {
		return null;
	}

	@Override
	public void add(IdCard businessLicense) {
		idCardMapper.insertSelective(businessLicense);
	}
	
	
}
