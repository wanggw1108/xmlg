package com.temporary.center.ls_common;

public class Constant {
	
	/**
	 * 有效的
	 */
	public static final String EFFECTIVE = "0";
	
	/**
	 * 方法开头
	 */
	public static final String METHOD_BEGIN=">>>>>>>>>>>>>>>>>>>>>>>";
	/**
	 * 方法结束
	 */
	public static final String   METHOD_END="<<<<<<<<<<<<<<<<<<<<<<<";
	
	
	
	/**
	 * 一天
	 */
	public static final int DAY = 24*60*60;

	/**
	 * 图片类型为  首页10个标签
	 */
	public static final String PICTURE_TYPE_TEN_TAB = "1";

	
	
	/**
	 * 降序
	 */
	public static final String DESC="desc";
	
	/**
	 * 升序
	 */
	public static final String ASC="asc";

	/**
	 * 五分钟  改成了3分钟
	 */
//	public static final long FIVE_MINUTE = 3*60*1000;

	
	
	
	/**
	 * 兼职类型
	 */
	public static final Integer PART_TIME_TYPE = 1;

	/**
	 * 审核中
	 */
	public static final Integer IN_AUDIT = 1;
	
	/**
	 * 审核完成
	 */
	public static final Integer AUDIT_COMPLETED=2;



	/**
	 * 一分钟
	 */
	public static final int ONE_MINUTE = 60;

	/**
	 * 是否全职  0：是 1：否
	 */
	public static final Integer IS_FULL_TIME = 0;
	
	/**
	 * 是否全职  0：是 1：否
	 */
	public static final Integer NOT_FULL_TIME = 1;

	/**
	 * 3分钟
	 */
//	public static final long THREE_MINUTE = 3*60*1000;

	/**
	 * 接口的超时时间设置 3分钟
	 */
	public static final long INTERFACE_TIME_OUT = 3*60*1000;

	/**
	 * token 的有效时间 单位 秒，5天
	 */
	public static final int TOKEN_EFFECTIVE_TIME = 5*24*60*60;

	/**
	 * 1：报名成功
	 */
	public static final Integer SIGN_UP_SUC = 1;
	
	/**
	 * 2：面试成功 
	 */
	public static final Integer INTERVIEW_SUC = 2;

	/**
	 * 用户默认头像地址
	 */
	public static final String USER_HEADER_IMAGE_URL_DEFAULT = "./images/images/user/header/user_header_default.jpeg";
	
	
	/**
	 * 招聘信息的状态 0：全部；1：进行中；2：下架；3：完结
	 */
	public static Integer RECRUITMENTINFO_RUNNING=1;
	
	/**
	 * 招聘信息的状态 0：全部；1：进行中；2：下架；3：完结
	 */
	public static Integer RECRUITMENTINFO_DOWN=2;
	
	/**
	 * 招聘信息的状态 0：全部；1：进行中；2：下架；3：完结
	 */
	public static Integer RECRUITMENTINFO_COMPLETE=3;
	
	
}
