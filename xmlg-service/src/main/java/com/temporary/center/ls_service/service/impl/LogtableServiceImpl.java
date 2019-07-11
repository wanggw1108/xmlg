package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.LogtableMapper;
import com.temporary.center.ls_service.domain.Logtable;
import com.temporary.center.ls_service.service.comm.LogtableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.SQLException;

@Service
public class LogtableServiceImpl implements LogtableService{

	@Autowired
	private LogtableMapper logtableMapper;
	
	@Override
	public boolean addLog(Logtable log) throws SQLException {
		return logtableMapper.insert(log)>0?true:false;
	}

}
