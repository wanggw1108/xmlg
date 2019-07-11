package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.City;

import java.util.List;

public interface CityMapper {
    int deleteByPrimaryKey(String city);

    int insert(City record);

    int insertSelective(City record);

    City selectByPrimaryKey(String city);

    int updateByPrimaryKeySelective(City record);

    int updateByPrimaryKey(City record);

	List<City> getCityALL();
}