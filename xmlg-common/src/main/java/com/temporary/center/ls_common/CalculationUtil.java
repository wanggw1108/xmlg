package com.temporary.center.ls_common;

public class CalculationUtil {
	
	public static void getTimeCount(long end, long start, LogUtil logger) {
		long count=end-start;
		if(count>1000) {
			logger.info("耗时{0}秒", count/1000);
		}else {
			logger.info("耗时{0}", count);
		}
	}
	
}
