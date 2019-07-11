package com.temporary.center.ls_service.common;

import com.temporary.center.ls_common.RedisBean;

public class TokenUtil {
	
	
	
	
	public static boolean validateToken(String token,RedisBean redisBean) {
		if(redisBean.exists(token)) {
			return true;
		}
		return false;
	}

}
