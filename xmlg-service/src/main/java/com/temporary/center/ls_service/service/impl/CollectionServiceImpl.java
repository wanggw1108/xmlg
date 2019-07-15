package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.CollectionMapper;
import com.temporary.center.ls_service.domain.MyCollection;
import com.temporary.center.ls_service.service.CollectionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CollectionServiceImpl implements CollectionService {

	@Autowired
	private CollectionMapper collectionMapper;

	@Override
	public void add(MyCollection myCollection) {
		collectionMapper.insertSelective(myCollection);
	}

	@Override
	public Long countByParam(MyCollection myCollection) {
		return Long.valueOf(collectionMapper.selectCount(myCollection));
	}

	@Override
	public List<MyCollection> findByParam(MyCollection myCollection) {
		return collectionMapper.select(myCollection);
	}
}
