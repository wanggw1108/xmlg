package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.Picture;

import java.util.List;

public interface PictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Picture record);

    int insertSelective(Picture record);

    Picture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Picture record);

    int updateByPrimaryKey(Picture record);

	List<Picture> getTenTab(String pictureTypeTenTab);
	
}