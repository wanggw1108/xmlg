package com.temporary.center.ls_service.domain;

import com.temporary.center.ls_service.common.Page;

import java.util.Date;

public class FullTime extends Page{
    private Long id;

    private String oneType;

    private String twoType;

    private String position;

    private Integer number;

    private Float salaryBegin;

    private Float salaryEnd;

    private String salaryUnit;

    private Integer ageBegin;

    private Integer ageEnd;

    private String academicRequirements;

    private String welfare;

    private String describe;

    private String environmentalPhotos;

    private String tel;

    private String wx;

    private String email;

    private Long createby;

    private Date createtime;

    private Long updateby;

    private Date updatetime;

    private Date starttime;

    private Date endtime;

    private String workSpace;

    private Integer sex;

    private Integer active;

    private String workingTime;

    private String longitude;

    private String latitude;

    private String experience;
    
    private String title;
    
    public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOneType() {
        return oneType;
    }

    public void setOneType(String oneType) {
        this.oneType = oneType == null ? null : oneType.trim();
    }

    public String getTwoType() {
        return twoType;
    }

    public void setTwoType(String twoType) {
        this.twoType = twoType == null ? null : twoType.trim();
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position == null ? null : position.trim();
    }

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public Float getSalaryBegin() {
        return salaryBegin;
    }

    public void setSalaryBegin(Float salaryBegin) {
        this.salaryBegin = salaryBegin;
    }

    public Float getSalaryEnd() {
        return salaryEnd;
    }

    public void setSalaryEnd(Float salaryEnd) {
        this.salaryEnd = salaryEnd;
    }

    public String getSalaryUnit() {
        return salaryUnit;
    }

    public void setSalaryUnit(String salaryUnit) {
        this.salaryUnit = salaryUnit == null ? null : salaryUnit.trim();
    }

    public Integer getAgeBegin() {
        return ageBegin;
    }

    public void setAgeBegin(Integer ageBegin) {
        this.ageBegin = ageBegin;
    }

    public Integer getAgeEnd() {
        return ageEnd;
    }

    public void setAgeEnd(Integer ageEnd) {
        this.ageEnd = ageEnd;
    }

    public String getAcademicRequirements() {
        return academicRequirements;
    }

    public void setAcademicRequirements(String academicRequirements) {
        this.academicRequirements = academicRequirements == null ? null : academicRequirements.trim();
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare == null ? null : welfare.trim();
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe == null ? null : describe.trim();
    }

    public String getEnvironmentalPhotos() {
        return environmentalPhotos;
    }

    public void setEnvironmentalPhotos(String environmentalPhotos) {
        this.environmentalPhotos = environmentalPhotos == null ? null : environmentalPhotos.trim();
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel == null ? null : tel.trim();
    }

    public String getWx() {
        return wx;
    }

    public void setWx(String wx) {
        this.wx = wx == null ? null : wx.trim();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email == null ? null : email.trim();
    }

    public Long getCreateby() {
        return createby;
    }

    public void setCreateby(Long createby) {
        this.createby = createby;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Long getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Long updateby) {
        this.updateby = updateby;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Date getStarttime() {
        return starttime;
    }

    public void setStarttime(Date starttime) {
        this.starttime = starttime;
    }

    public Date getEndtime() {
        return endtime;
    }

    public void setEndtime(Date endtime) {
        this.endtime = endtime;
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace == null ? null : workSpace.trim();
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime == null ? null : workingTime.trim();
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude == null ? null : longitude.trim();
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude == null ? null : latitude.trim();
    }
}