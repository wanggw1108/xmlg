package com.temporary.center.ls_service.common;

import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;

public class TokenUtil {
	
	
	
	
	public static boolean validateToken(String token,RedisBean redisBean) {
		if(redisBean.exists(RedisKey.USER_TOKEN+token)) {
			return true;
		}
		return false;
	}

}
