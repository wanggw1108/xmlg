package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.JoinMapper;
import com.temporary.center.ls_service.domain.Join;
import com.temporary.center.ls_service.service.JoinService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class JoinServiceImpl implements JoinService {
	
	@Autowired
	private JoinMapper joinMapper;

	@Override
	public void join(Join join) {
		joinMapper.insertSelective(join);
	}

	@Override
	public Long countByParam(Join join) {
		return null;
	}

	@Override
	public List<Join> findDataByParam(Join joinParam) {
		return null;
	}
	
	
	
}
