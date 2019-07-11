/**
 * 
 */
package com.temporary.center.ls_common;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

@Component
@Scope("singleton")
public class SpringContextUtils implements ApplicationContextAware {

	private static ApplicationContext context;

	/**
	 * 根据类型获取bean
	 * 
	 * @param requiredType
	 * @return
	 */
	public static <T> T getBean(Class<T> requiredType) {
		return context.getBean(requiredType);
	}

	/**
	 * 根据名称获取bean
	 * 
	 * @param name
	 * @param requiredType
	 * @return
	 */
	public static <T> T getBean(String name, Class<T> requiredType) {
		return context.getBean(name, requiredType);
	}
	/**
	 * 根据名称获取bean
	 * 
	 * @param name
	 * @return
	 */
	public static Object getBean(String name) {
		return context.getBean(name);
	}

	/**
	 * 获取某个类型的所有bean
	 * 
	 * @param requiredType
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T> T[] getBeans(Class<T> requiredType) {
		Collection<T> collection = context.getBeansOfType(requiredType).values();

		if (collection.size() > 0) {
			T[] arr = (T[]) Array.newInstance(requiredType, collection.size());

			int x = 0;
			Iterator<T> it = collection.iterator();
			while (it.hasNext()) {
				arr[x++] = it.next();
			}

			return arr;
		}

		return null;
	}

	/**
	 * 获取 #ApplicationContext 实例
	 * 
	 * @return
	 */
	public static ApplicationContext getContext() {
		return context;
	}

	/**
	 * 设置 #ApplicationContext 实例
	 * 
	 * @param applicationContext
	 */
	public static void setContext(ApplicationContext applicationContext) {
		context = applicationContext;
	}

	/**
	 * 注入 #ApplicationContext 实例
	 */
	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		context = applicationContext;
	}

}
