package com.temporary.center.ls_common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class RequestUtil {
	
	private static final LogUtil logger = LogUtil.getLogUtil(RequestUtil.class);
	
	public static String getSessionId(HttpServletRequest request) {
		HttpSession session = request.getSession();
		if(null!=session) {
			return session.getId();
		}
		logger.warn("HttpServletRequest 的session 为空");
		return null;
	}

}
