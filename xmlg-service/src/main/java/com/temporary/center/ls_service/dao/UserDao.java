package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.User;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;

public interface UserDao extends Mapper<User> {


	List<User> queryUserByParams(Map<String, Object> params);


	Long updatePassword(Map<String, Object> params);


	User getUserById(long parseLong);


	void createUser(User user);


	Long countDataByParams(User userParam);


	void updateUser(Map<String, Object> param);
	
}
