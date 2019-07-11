package com.temporary.center.ls_service.common;

import com.temporary.center.ls_common.LogUtil;

import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ValidateParam {

	private static final LogUtil logger = LogUtil.getLogUtil(ValidateParam.class);
	
	public static String NEED="need";
	public static String NOT_NEED="not_need";
	
	/**
	 * 
	 * @param param 
	 * @param type ValidateParam.NEED 需要检查的字段   ValidateParam.NOT_NEED 不需要检查的字段  。如果此字段为空 检查所有字段
	 * @param set 字段
	 * @return
	 */
	public static Json validate(Map<String,String> param,String type,Set<String> set) {
		Json json=new Json();
		json.setSuc();
		if(null==param || param.size()==0) {
			json.setSattusCode(StatusCode.PARAMS_NO_NULL);
			return json;
		}
		
		Set<Entry<String, String>> entrySet = param.entrySet();
		Iterator<Entry<String, String>> iterator = entrySet.iterator();

		while(iterator.hasNext()) {
			Entry<String, String> next = iterator.next();
			String key = next.getKey();
			//判断是否需要检查
			if(null!=type) {
				if(NEED.equals(type)) {
					if(null!=set && set.size()!=0 && !set.contains(key)) {
						continue;
					}
				}
				if(NOT_NEED.equals(type)) {
					if(null!=set && set.size()!=0 && set.contains(key)) {
						continue;
					}
				}
			}
			
			String value = next.getValue();
			if(null==value || value.equals("")) {
				json.setSattusCode(StatusCode.PARAMS_NO_NULL);
				json.setMsg(json.getMsg()+"("+key+")");
				break;
			}
		}
		return json;
	}
	
	@SuppressWarnings("rawtypes")
	public static Json validateParamNotNull(Object o) throws IllegalArgumentException, IllegalAccessException {
		Class class1=o.getClass();
		Json json=new Json();
		json.setSuc();
		Field[] fields = class1.getDeclaredFields();
		for(Field field:fields) {
			String name = field.getName();
			field.setAccessible(true);
			NotNullEmpty annotation = field.getAnnotation(NotNullEmpty.class);
			if(null!=annotation ) {
				Object object = field.get(o);
				if(null==object || object.equals("")) {
					logger.info(StatusCode.PARAMS_NO_NULL.getMessage()+"("+name+")");
					json.setSattusCode(StatusCode.PARAMS_NO_NULL);
					json.setMsg(StatusCode.PARAMS_NO_NULL.getMessage()+"("+name+")");
					json.setData(StatusCode.PARAMS_NO_NULL.getMessage());
					break;
				}
			}
		}
		return json;
	}
	
	
	
}
