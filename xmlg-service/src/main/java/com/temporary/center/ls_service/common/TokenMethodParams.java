package com.temporary.center.ls_service.common;

public class TokenMethodParams {

	private String token;
	private String sigin;
	private String time;
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getSigin() {
		return sigin;
	}
	public void setSigin(String sigin) {
		this.sigin = sigin;
	}
	public String getTime() {
		return time;
	}
	public void setTime(String time) {
		if(null!=time) {
			System.out.println(token);
			System.out.println(sigin);
		}
		this.time = time;
	}
	
}
