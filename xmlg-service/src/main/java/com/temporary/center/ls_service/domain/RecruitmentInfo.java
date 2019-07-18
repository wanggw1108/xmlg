package com.temporary.center.ls_service.domain;

import com.temporary.center.ls_service.common.NotNullEmpty;
import com.temporary.center.ls_service.common.Page;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;


@Table(name="recruitment_info")
public class RecruitmentInfo extends Page{
	@Id
	@GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNullEmpty
    private String recruitmentCategory;

    @NotNullEmpty
    private String title;

    @NotNullEmpty
    private Float basePay;

    @NotNullEmpty
    private String basePayUnit;

    private String workPlace;

    private String sex;

    private String type;

    private String typeWork;

    private String longitude;

    private String latitude;

    @NotNullEmpty
    private Integer number;

    private String workingStartTime;

    private String workingEndTime;

    private String workingTime;

    private String settlementMethod;

    private String detail;

    private String createby;

    private Date createtime;

    private String updateby;

    private Date updatetime;

    private Integer active;

    private String recruitmentStartTime;

    private String recruitmentEndTime;

    @NotNullEmpty
    private String welfare;

    private String isFullTime;

    private String oneType;

    private String twoType;

    private String position;

    private Float salaryBegin;

    private Float salaryEnd;

    private Integer joinMumber;

    private Integer employeeReputation;
	
	private Integer bossReputation;
    
	private String salaryUnit;
	
	private Integer ageBegin;
	
	private Integer ageEnd;
	
	private String academicRequirements;
	
	private String experience;
	
	private String environmentalPhotos;
	
	private String tel;
	
	private String wx;
	
	private String email;
	
	private String contactsName;
	
	/**
	 * 招聘人的性别 1是男；2是女
	 */
	private String createSex;
	
	/**
	 * 用户头像地址url
	 */
	private String userImageUrl;
	
	/**
	 * 浏览次数 默认是0次
	 */
	private Long browseTime=0L;
	
	/**
	 * 已经招聘人数
	 */
	private Integer recruitment;
	
	/**
	 * 距离
	 */
	private int distance;
	
	/**
	 * 时薪
	 */
	private Float hourlyWage;
	
	/**
	 * 区域
	 */
	@NotNullEmpty
	private String district;


	
	/**
	 * 招聘信息的状态 0：全部；1：进行中；2：下架；3：完结
	 */
	private Integer state;

	private String search_text;
	
	public Integer getState() {
		return state;
	}

	public void setState(Integer state) {
		this.state = state;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public Float getHourlyWage() {
		return hourlyWage;
	}

	public void setHourlyWage(Float hourlyWage) {
		this.hourlyWage = hourlyWage;
	}

	public int getDistance() {
		return distance;
	}

	public void setDistance(int distance) {
		this.distance = distance;
	}

	public Integer getRecruitment() {
		return recruitment;
	}

	public void setRecruitment(Integer recruitment) {
		this.recruitment = recruitment;
	}

	public Long getBrowseTime() {
		return browseTime;
	}

	public void setBrowseTime(Long browseTime) {
		this.browseTime = browseTime;
	}

	public String getUserImageUrl() {
		return userImageUrl;
	}

	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}

	public String getCreateSex() {
		return createSex;
	}

	public void setCreateSex(String createSex) {
		this.createSex = createSex;
	}

	public String getContactsName() {
		return contactsName;
	}

	public void setContactsName(String contactsName) {
		this.contactsName = contactsName;
	}


	public String getOneType() {
		return oneType;
	}

	public void setOneType(String oneType) {
		this.oneType = oneType;
	}

	public String getTwoType() {
		return twoType;
	}

	public void setTwoType(String twoType) {
		this.twoType = twoType;
	}

	public String getPosition() {
		return position;
	}

	public void setPosition(String position) {
		this.position = position;
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
		this.salaryUnit = salaryUnit;
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
		this.academicRequirements = academicRequirements;
	}

	public String getExperience() {
		return experience;
	}

	public void setExperience(String experience) {
		this.experience = experience;
	}

	public String getEnvironmentalPhotos() {
		return environmentalPhotos;
	}

	public void setEnvironmentalPhotos(String environmentalPhotos) {
		this.environmentalPhotos = environmentalPhotos;
	}

	public String getTel() {
		return tel;
	}

	public void setTel(String tel) {
		this.tel = tel;
	}

	public String getWx() {
		return wx;
	}

	public void setWx(String wx) {
		this.wx = wx;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Integer getEmployeeReputation() {
		return employeeReputation;
	}

	public void setEmployeeReputation(Integer employeeReputation) {
		this.employeeReputation = employeeReputation;
	}

	public Integer getBossReputation() {
		return bossReputation;
	}

	public void setBossReputation(Integer bossReputation) {
		this.bossReputation = bossReputation;
	}

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
        this.title = title == null ? null : title.trim();
    }

    public Float getBasePay() {
        return basePay;
    }

    public void setBasePay(Float basePay) {
        this.basePay = basePay;
    }

    public String getBasePayUnit() {
        return basePayUnit;
    }

    public void setBasePayUnit(String basePayUnit) {
        this.basePayUnit = basePayUnit == null ? null : basePayUnit.trim();
    }

    public String getWorkPlace() {
        return workPlace;
    }

    public void setWorkPlace(String workPlace) {
        this.workPlace = workPlace == null ? null : workPlace.trim();
    }

	public String getRecruitmentCategory() {
		return recruitmentCategory;
	}

	public void setRecruitmentCategory(String recruitmentCategory) {
		this.recruitmentCategory = recruitmentCategory;
	}

	public String getSex() {
		return sex;
	}

	public void setSex(String sex) {
		this.sex = sex;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public void setSettlementMethod(String settlementMethod) {
		this.settlementMethod = settlementMethod;
	}

	public String getIsFullTime() {
		return isFullTime;
	}

	public void setIsFullTime(String isFullTime) {
		this.isFullTime = isFullTime;
	}

	public String getTypeWork() {
        return typeWork;
    }

    public void setTypeWork(String typeWork) {
        this.typeWork = typeWork == null ? null : typeWork.trim();
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

    public Integer getNumber() {
        return number;
    }

    public void setNumber(Integer number) {
        this.number = number;
    }

    public String getWorkingStartTime() {
		return workingStartTime;
	}

	public void setWorkingStartTime(String workingStartTime) {
		this.workingStartTime = workingStartTime;
	}

	public String getWorkingEndTime() {
		return workingEndTime;
	}

	public void setWorkingEndTime(String workingEndTime) {
		this.workingEndTime = workingEndTime;
	}

	public String getWorkingTime() {
        return workingTime;
    }

    public void setWorkingTime(String workingTime) {
        this.workingTime = workingTime == null ? null : workingTime.trim();
    }

	public String getSettlementMethod() {
		return settlementMethod;
	}

	public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail == null ? null : detail.trim();
    }

    public String getCreateby() {
        return createby;
    }

    public void setCreateby(String createby) {
        this.createby = createby == null ? null : createby.trim();
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getUpdateby() {
        return updateby;
    }

    public void setUpdateby(String updateby) {
        this.updateby = updateby == null ? null : updateby.trim();
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }

    public Integer getActive() {
        return active;
    }

    public void setActive(Integer active) {
        this.active = active;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare == null ? null : welfare.trim();
    }

    public Integer getJoinMumber() {
        return joinMumber;
    }

    public void setJoinMumber(Integer joinMumber) {
        this.joinMumber = joinMumber;
    }

	public String getRecruitmentStartTime() {
		return recruitmentStartTime;
	}

	public void setRecruitmentStartTime(String recruitmentStartTime) {
		this.recruitmentStartTime = recruitmentStartTime;
	}

	public String getRecruitmentEndTime() {
		return recruitmentEndTime;
	}

	public void setRecruitmentEndTime(String recruitmentEndTime) {
		this.recruitmentEndTime = recruitmentEndTime;
	}

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}
}