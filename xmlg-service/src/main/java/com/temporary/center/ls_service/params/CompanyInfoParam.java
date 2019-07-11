package com.temporary.center.ls_service.params;

import com.temporary.center.ls_service.common.NotNullEmpty;

import java.util.Date;

public class CompanyInfoParam {
    private Long companyId;

    private String companyPortraitUrl;

    @NotNullEmpty
    private String companyName;

    
    private Integer companyIsAuth;

    @NotNullEmpty
    private String companyIndustry;

    @NotNullEmpty
    private String companyScale;

    @NotNullEmpty
    private String companyAddress;

    @NotNullEmpty
    private String companyIntroduction;

    private String businessLicenseUrl;

    @NotNullEmpty
    private String qualificationsOneUrl;

    private String qualificationsTwoUrl;

    private String qualificationsThreeUrl;

    private Long createBy;

    private Date createTime;

    private Long updateBy;

    private Date updateTime;

    /**
     * 营业执照文件路径
     */
    private String fileName;
    
    /**
     * 社会信用代码
     */
    private String companyCode;
    
    /**
     * 公司联系电话
     */
    @NotNullEmpty
    private String companyNumber;
    
    /**
     * 经度
     */
    @NotNullEmpty
    private String longitude;
    
    /**
     * 维度
     */
    @NotNullEmpty
    private String latitude;
    
    /**
     * 公司类型 0：公司；1：团队
     */
    @NotNullEmpty
    private String companyType;
    
    /**
     * 不通过原因
     */
    private String reason;
    
    /**
     * 雇主信誉
     */
    private String employerReputation;

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public String getCompanyCode() {
		return companyCode;
	}

	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getCompanyNumber() {
		return companyNumber;
	}

	public void setCompanyNumber(String companyNumber) {
		this.companyNumber = companyNumber;
	}

	public String getCompanyType() {
		return companyType;
	}

	public void setCompanyType(String companyType) {
		this.companyType = companyType;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getEmployerReputation() {
		return employerReputation;
	}

	public void setEmployerReputation(String employerReputation) {
		this.employerReputation = employerReputation;
	}

	public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }

    public String getCompanyPortraitUrl() {
        return companyPortraitUrl;
    }

    public void setCompanyPortraitUrl(String companyPortraitUrl) {
        this.companyPortraitUrl = companyPortraitUrl == null ? null : companyPortraitUrl.trim();
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName == null ? null : companyName.trim();
    }

    public Integer getCompanyIsAuth() {
        return companyIsAuth;
    }

    public void setCompanyIsAuth(Integer companyIsAuth) {
        this.companyIsAuth = companyIsAuth;
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry == null ? null : companyIndustry.trim();
    }

    public String getCompanyScale() {
        return companyScale;
    }

    public void setCompanyScale(String companyScale) {
        this.companyScale = companyScale == null ? null : companyScale.trim();
    }

    public String getCompanyAddress() {
        return companyAddress;
    }

    public void setCompanyAddress(String companyAddress) {
        this.companyAddress = companyAddress == null ? null : companyAddress.trim();
    }

    public String getCompanyIntroduction() {
        return companyIntroduction;
    }

    public void setCompanyIntroduction(String companyIntroduction) {
        this.companyIntroduction = companyIntroduction == null ? null : companyIntroduction.trim();
    }

    public String getBusinessLicenseUrl() {
        return businessLicenseUrl;
    }

    public void setBusinessLicenseUrl(String businessLicenseUrl) {
        this.businessLicenseUrl = businessLicenseUrl == null ? null : businessLicenseUrl.trim();
    }

    public String getQualificationsOneUrl() {
        return qualificationsOneUrl;
    }

    public void setQualificationsOneUrl(String qualificationsOneUrl) {
        this.qualificationsOneUrl = qualificationsOneUrl == null ? null : qualificationsOneUrl.trim();
    }

    public String getQualificationsTwoUrl() {
        return qualificationsTwoUrl;
    }

    public void setQualificationsTwoUrl(String qualificationsTwoUrl) {
        this.qualificationsTwoUrl = qualificationsTwoUrl == null ? null : qualificationsTwoUrl.trim();
    }

    public String getQualificationsThreeUrl() {
        return qualificationsThreeUrl;
    }

    public void setQualificationsThreeUrl(String qualificationsThreeUrl) {
        this.qualificationsThreeUrl = qualificationsThreeUrl == null ? null : qualificationsThreeUrl.trim();
    }

    public Long getCreateBy() {
        return createBy;
    }

    public void setCreateBy(Long createBy) {
        this.createBy = createBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Long getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(Long updateBy) {
        this.updateBy = updateBy;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
    
}