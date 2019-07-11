package com.temporary.center.ls_service.common;

/**
 * JSON模型
 * 
 * @author  
 * 
 */
public class Json implements java.io.Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String msg = "";// 提示信息
	private Object data = "";// 其他信息
	private String code;
	
	
	
	//private String debugMsg="";//调试信息
	
	/*public String getDebugMsg() {
		return debugMsg;
	}

	public void setDebugMsg(String debugMsg) {
		this.debugMsg = debugMsg;
	}*/

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public Object getData() {
		return data;
	}

	public void setData(Object data) {
		this.data = data;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public void setFail() {
		this.code=StatusCode.FAIL.getCode();
		this.msg=StatusCode.FAIL.getMessage();
	}

	public void setSuc() {
		this.code=StatusCode.SUC.getCode();
		this.msg=StatusCode.SUC.getMessage();

	}
	public void setSuc(String msg) {
		this.code=StatusCode.SUC.getCode();
		this.msg=msg;
	}

	public void setSattusCode(StatusCode statusCode) {
		this.code=statusCode.getCode();
		this.msg=statusCode.getMessage();
	}
	  
}
