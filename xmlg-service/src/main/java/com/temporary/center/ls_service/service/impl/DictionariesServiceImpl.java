package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.DictionariesMapper;
import com.temporary.center.ls_service.domain.Dictionaries;
import com.temporary.center.ls_service.service.DictionariesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DictionariesServiceImpl implements DictionariesService {

	@Autowired
	private DictionariesMapper dictionariesMapper;
	
	@Override
	public List<Dictionaries> list(Dictionaries dictionaries) {
		return dictionariesMapper.select(dictionaries);
	}
	
	
	
}
