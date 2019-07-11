package com.temporary.center.ls_service.common;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 标志不能为null或者空字符串
 * @author  fuyuanming
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface NotNullEmpty {
	public String id() default "NotNullEmpty";
    public String description() default "no description";	
}
