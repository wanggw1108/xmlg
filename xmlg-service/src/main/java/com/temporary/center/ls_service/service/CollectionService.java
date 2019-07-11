package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.MyCollection;

import java.util.List;

public interface CollectionService {

	void add(MyCollection myCollection);

	Long countByParam(MyCollection myCollection);

	List<MyCollection> findByParam(MyCollection myCollection);

}
