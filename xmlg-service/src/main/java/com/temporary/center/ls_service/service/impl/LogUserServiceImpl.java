package com.temporary.center.ls_service.service.impl;

import com.temporary.center.ls_service.dao.UserAddressMapper;
import com.temporary.center.ls_service.dao.UserDao;
import com.temporary.center.ls_service.domain.User;
import com.temporary.center.ls_service.domain.UserAddress;
import com.temporary.center.ls_service.service.LogUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class LogUserServiceImpl implements LogUserService {
	
	@Autowired
	private UserDao userDao;
	
	@Autowired
	private UserAddressMapper userAddressMapper;


	@Override
	public User selectOne(User user) {
		return userDao.selectOne(user);
	}

	@Override
	public List<User> login(Map<String,Object> params) {
		return userDao.queryUserByParams(params);
	}

	@Override
	public Long updatePassword(Map<String, Object> params) {
		Long result=userDao.updatePassword(params);
		return result;
	}

	@Override
	public User getUserById(long parseLong) {
		return userDao.getUserById(parseLong);
	}

	@Override
	public void createUser(User user) {
		userDao.createUser(user);
	}

	@Override
	public Long countDataByParams(User userParam) {
		return userDao.countDataByParams(userParam);
	}

	@Override
	public void updateUser(Map<String, Object> param) {
		userDao.updateUser(param);
	}

	@Override
	public List<UserAddress> addressList(UserAddress userAddress) {
		return userAddressMapper.list(userAddress);
	}

	@Override
	public void addAddress(UserAddress userAddress) {
		userAddressMapper.insertSelective(userAddress);
	}

	@Override
	public void updateUserAddress(UserAddress updateUserAddress) {
		userAddressMapper.updateByPrimaryKeySelective(updateUserAddress);
	}

	@Override
	public void updateIsdefault(UserAddress updateUserAddress) {
		userAddressMapper.updateIsdefault(updateUserAddress);
	}

}
