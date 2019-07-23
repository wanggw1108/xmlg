package com.temporary.center.ls_service.result;

/**
 * 我的招聘
 * @author  fuyuanming
 */
public class MyRecruitResult {
	private Integer id;
	private String title;
	private String state;
	private String signUpNumber;
	private String needRecruitNumber;
	private String recruitedNumber;
	private String basePay;
	private String basePayUnit;
	private String releaseTime;
	private String browse;

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getState() {
		return state;
	}
	public void setState(String state) {
		this.state = state;
	}
	public String getSignUpNumber() {
		return signUpNumber;
	}
	public void setSignUpNumber(String signUpNumber) {
		this.signUpNumber = signUpNumber;
	}
	public String getNeedRecruitNumber() {
		return needRecruitNumber;
	}
	public void setNeedRecruitNumber(String needRecruitNumber) {
		this.needRecruitNumber = needRecruitNumber;
	}
	public String getRecruitedNumber() {
		return recruitedNumber;
	}
	public void setRecruitedNumber(String recruitedNumber) {
		this.recruitedNumber = recruitedNumber;
	}
	public String getBasePay() {
		return basePay;
	}
	public void setBasePay(String basePay) {
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
	public String getBrowse() {
		return browse;
	}
	public void setBrowse(String browse) {
		this.browse = browse;
	}
}
