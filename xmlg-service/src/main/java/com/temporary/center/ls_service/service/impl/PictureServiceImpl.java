package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.PictureMapper;
import com.temporary.center.ls_service.domain.Picture;
import com.temporary.center.ls_service.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class PictureServiceImpl implements PictureService{

	@Autowired
	private PictureMapper pictureMapper;
	
	@Override
	public List<Picture> getTenTab(String pictureTypeTenTab) {

		Example example = new Example(Picture.class);
		Example.Criteria c = example.createCriteria();
		c.andEqualTo("type",pictureTypeTenTab);
		example.setOrderByClause("sort asc");
		return pictureMapper.selectByExample(example);
	}

}
