package com.temporary.center.ls_service.common;

import com.temporary.center.ls_common.LogUtil;

/**
 * 管理签名
 * @author  fuyuanming
 *
 */
public class SignUtil {

	private static final LogUtil logger = LogUtil.getLogUtil(SignUtil.class);

	
	/**
	 * 是否打开 签名验证
	 * @return
	 */
	public static Boolean isOpen() {
		logger.info("是否打开 签名验证:false");
		return false;
	}
	
}
