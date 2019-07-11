package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.IdCard;

import java.util.List;

public interface IdCardService {

	List<IdCard> list(IdCard businessLicense);

	void add(IdCard businessLicense);

}
