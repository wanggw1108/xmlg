package com.temporary.center.ls_service.result;

import java.util.List;

/**
 * 查看报名 返回的结果
 * @author  fuyuanming
 */
public class JoinResult {

	//雇主头像的URL
	private String portraitUrl;
	//招聘信息的标题
	private String title;
	//工资
	private Integer basePay;
	//工资单位
	private String basePayUnit;
	//发布时间
	private String releaseTime;
	//	浏览次数
	private String browseNumber;
	//	报名人数
	private String signUpNumber;
	//雇员信息
	private List<SignUpEmployeeInfo> signUpEmployeeInfo;
	public String getPortraitUrl() {
		return portraitUrl;
	}
	public void setPortraitUrl(String portraitUrl) {
		this.portraitUrl = portraitUrl;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public Integer getBasePay() {
		return basePay;
	}
	public void setBasePay(Integer basePay) {
		this.basePay = basePay;
	}
	public String getBasePayUnit() {
		return basePayUnit;
	}
	public void setBasePayUnit(String basePayUnit) {
		this.basePayUnit = basePayUnit;
	}
	public String getReleaseTime() {
		return releaseTime;
	}
	public void setReleaseTime(String releaseTime) {
		this.releaseTime = releaseTime;
	}
	public String getBrowseNumber() {
		return browseNumber;
	}
	public void setBrowseNumber(String browseNumber) {
		this.browseNumber = browseNumber;
	}
	public String getSignUpNumber() {
		return signUpNumber;
	}
	public void setSignUpNumber(String signUpNumber) {
		this.signUpNumber = signUpNumber;
	}
	public List<SignUpEmployeeInfo> getSignUpEmployeeInfo() {
		return signUpEmployeeInfo;
	}
	public void setSignUpEmployeeInfo(List<SignUpEmployeeInfo> signUpEmployeeInfo) {
		this.signUpEmployeeInfo = signUpEmployeeInfo;
	}

}
