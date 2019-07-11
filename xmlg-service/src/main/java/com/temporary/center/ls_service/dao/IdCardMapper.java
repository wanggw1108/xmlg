package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.IdCard;

import java.util.List;

public interface IdCardMapper {
    int insert(IdCard record);

    int insertSelective(IdCard record);

	List<IdCard> list(IdCard businessLicense);
}