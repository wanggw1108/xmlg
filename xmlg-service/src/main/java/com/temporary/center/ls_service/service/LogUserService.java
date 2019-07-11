package com.temporary.center.ls_service.service;

import com.temporary.center.ls_service.domain.User;
import com.temporary.center.ls_service.domain.UserAddress;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface LogUserService {


	User selectOne(User user);

	/***
	 * 登录
	 * @param params
	 * @return
	 */
	List<User> login(Map<String, Object> params);

	/**
	 * 修改密码
	 * @param params
	 * @return
	 */
	Long updatePassword(Map<String, Object> params);
	
	/**
	 * 根据ID获取用户信息
	 * @param parseLong
	 * @return
	 */
	User getUserById(long parseLong);

	/**
	 * 注册用户
	 * @param user
	 */
	void createUser(User user);
	
	Long countDataByParams(User userParam);

	/**
	 * 修改信息
	 * @param param
	 */
	void updateUser(Map<String, Object> param);

	/**
	 * 查询用户地址列表
	 * @param userAddress
	 * @return
	 */
	List<UserAddress> addressList(UserAddress userAddress);

	/**
	 * 新增用户地址
	 * @param userAddress
	 */
	void addAddress(UserAddress userAddress);

	/**
	 * 编辑用户地址
	 * @param updateUserAddress
	 */
	void updateUserAddress(UserAddress updateUserAddress);

	/**
	 * 修改用户默认值
	 * @param updateUserAddress
	 */
	void updateIsdefault(UserAddress updateUserAddress);
	
}
