package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.CarouselPictureMapper;
import com.temporary.center.ls_service.domain.CarouselPicture;
import com.temporary.center.ls_service.service.CarouselPictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CarouselPictureServiceImpl implements CarouselPictureService {

	@Autowired
	private CarouselPictureMapper carouselPictureMapper;
	
	@Override
	public List<CarouselPicture> getALL() {
		return carouselPictureMapper.getALL();
	}

}
