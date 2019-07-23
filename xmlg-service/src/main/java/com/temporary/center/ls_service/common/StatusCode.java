package com.temporary.center.ls_service.common;

public enum StatusCode {
	
	NO_DATA("0000","无数据"), 
	NO_TIME_STAMP("001","没有时间戳"),
	NO_SIGN("002","没有签名sign"),

	SUC("100", "请求成功"),
	
	FAIL("200", "请求失败"),
	
	PROGRAM_EXCEPTION("300","程序异常"),
	JOIN_ERROR("301","已报名"),

	TOKEN_ERROR("401","token过期"), 
	SIGN_ERROR("403","sign签名错误"),
	SMS_ERROR("402","一分钟后再发送"),
	SMS_NULL_ERROR("404","验证码超时，请重发"),
	SMS_CODE_ERROR("405","验证码错误"),
	SMS_TIMES_ERROR("406","请求验证码频繁，五分钟最多三次"),
	SMS_CHECK_ERROR("407","验证失败"),//请求验证异常
	AUTH_CHECK_ERROR("408","身份认证失败，请确保身份证清晰"),//请求验证异常
	AUTH_Company_ERROR("409","营业执照验证失败，请确保执照清晰"),//请求验证异常

	TIME_OUT_FIVE_MINUTE("502","操作失败-时间过期3分钟之内才有效"), 
	SIG_ERROR("503","签名失败"), 
	
	PARAMS_NO_NULL("600","参数不为空"), 
	TEL_REGISTERED("601","此号码已注册"),
	USER_NAME_REGISTERED("602","此用户名已注册"),
	USER_NULL_REGISTERED("603","此号码未注册"),

	JG_NO_DATA("602","极光不存在此用户"), 
	JG_HAVE_DATA("603","极光已存在此用户"),
	LOGIN_TYPE_ERROR("604","登录类型错误"),
	JG_Regist_Error("605","极光用户注册失败"),

	AMOUNT_NULL("700","金额不为空"), 
	TYPE_NULL("701","类型不为空"), 
	REMARK_NULL("703","备注不为空"), 
	
	USERNAME_PASS_ERROR("800","用户名或密码有误"), 
	DATA_ERROR("801","数据有误"), 
	EXIST_DATA("802","数据已存在"),
	ThirdID_ERROR("803","三方ID错误");


	private String code;
	private String message;
	
    StatusCode(String code,String msg) {
		this.code=code;
		this.message=msg;
	}
	
	public String getCode() {
        return this.code;
    }
    public String getMessage() {
        return this.message;
    }
}
