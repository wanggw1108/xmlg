package com.temporary.center.ls_service.dao;

import com.temporary.center.ls_service.domain.User;
import org.springframework.stereotype.Component;
import tk.mybatis.mapper.common.Mapper;

import java.util.List;
import java.util.Map;
@Component
public  interface UserDao extends Mapper<User> {


	public abstract List<User> queryUserByParams(Map<String, Object> params);


	public abstract Long updatePassword(Map<String, Object> params);


	public abstract User getUserById(long parseLong);


	public abstract void createUser(User user);


	public abstract Long countDataByParams(User userParam);


	public abstract void updateUser(Map<String, Object> param);
	
}
