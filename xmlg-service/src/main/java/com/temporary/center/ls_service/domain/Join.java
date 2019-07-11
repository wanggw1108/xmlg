package com.temporary.center.ls_service.domain;

import com.temporary.center.ls_service.common.Page;

import java.util.Date;

public class Join extends Page{
    private Long id;

    private Long resumeId;

    private String userName;

    private Integer userId;

    private Long recruitmentInfoId;

    private String recruitmentInfoCreateby;

    private String recruitmentInfoCreatebyName;

    private Date createtime;

    private String remark;

    private Integer state;

	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getResumeId() {
        return resumeId;
    }

    public void setResumeId(Long resumeId) {
        this.resumeId = resumeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Long getRecruitmentInfoId() {
        return recruitmentInfoId;
    }

    public void setRecruitmentInfoId(Long recruitmentInfoId) {
        this.recruitmentInfoId = recruitmentInfoId;
    }

    public String getRecruitmentInfoCreateby() {
        return recruitmentInfoCreateby;
    }

    public void setRecruitmentInfoCreateby(String recruitmentInfoCreateby) {
        this.recruitmentInfoCreateby = recruitmentInfoCreateby == null ? null : recruitmentInfoCreateby.trim();
    }

    public String getRecruitmentInfoCreatebyName() {
        return recruitmentInfoCreatebyName;
    }

    public void setRecruitmentInfoCreatebyName(String recruitmentInfoCreatebyName) {
        this.recruitmentInfoCreatebyName = recruitmentInfoCreatebyName == null ? null : recruitmentInfoCreatebyName.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }
}