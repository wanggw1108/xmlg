package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.Dictionaries;

import java.util.List;

public interface DictionariesMapper {
    int insert(Dictionaries record);

    int insertSelective(Dictionaries record);

	List<Dictionaries> list(Dictionaries dictionaries);
}