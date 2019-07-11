package com.temporary.center.ls_service.common;

import java.lang.reflect.Field;

public class ClassUtil {

	
	/**
	 * 打印Object 的值
	 * @param o
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	public static String  printlnFieldValue(Object o) throws IllegalArgumentException, IllegalAccessException {
		StringBuffer sBuffer=new StringBuffer();
		Class class1=o.getClass();
		Json json=new Json();
		json.setSuc();
		Field[] fields = class1.getDeclaredFields();
		for(Field field:fields) {
			String name = field.getName();
			field.setAccessible(true);
			Object object = field.get(o);
			if(null==object) {
				continue;
			}
			sBuffer.append(name+"="+object+", ");
		}
		return sBuffer.toString();
	}
	
}
