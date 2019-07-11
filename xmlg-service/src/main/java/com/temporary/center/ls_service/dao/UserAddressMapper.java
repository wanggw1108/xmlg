package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.UserAddress;

import java.util.List;

public interface UserAddressMapper {
    int deleteByPrimaryKey(Long id);

    int insert(UserAddress record);

    int insertSelective(UserAddress record);

    UserAddress selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserAddress record);

    int updateByPrimaryKey(UserAddress record);

	List<UserAddress> list(UserAddress userAddress);
	
	void updateIsdefault(UserAddress updateUserAddress);
}