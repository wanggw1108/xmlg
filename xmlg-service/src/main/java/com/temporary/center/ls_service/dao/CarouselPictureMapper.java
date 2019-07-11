package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.CarouselPicture;


import java.util.List;

public interface CarouselPictureMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(CarouselPicture record);

    int insertSelective(CarouselPicture record);

    CarouselPicture selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(CarouselPicture record);

    int updateByPrimaryKey(CarouselPicture record);

	List<CarouselPicture> getALL();
}