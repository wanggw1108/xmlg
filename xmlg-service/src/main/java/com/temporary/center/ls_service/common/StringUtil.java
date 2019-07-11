package com.temporary.center.ls_service.common;

public class StringUtil {

	/**
	 * 当字符串为空或者为字符串null 的时候返回  空字符串，其他的则返回原值
	 * @param str
	 * @return
	 */
	public static String  nullString(String str) {
		if(null==str || "null".equals(str.toLowerCase())) {
			return "";
		}
		return str;
	}
	
}
