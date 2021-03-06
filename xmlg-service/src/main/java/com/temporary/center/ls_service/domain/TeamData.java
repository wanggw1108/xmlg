package com.temporary.center.ls_service.domain;

import javax.persistence.*;
import java.util.Date;
@Table(name="team_data")
public class TeamData {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private String type;

    private String name;

    private String industry;

    private String scale;

    private String introduce;

    private String address;

    private String longitude;

    private String latitude;
    @Column(name="create_by")
    private String createBy;
    @Column(name="create_time")
    private Date createtime;
    @Column(name="update_by")
    private String updateby;
    @Column(name="update_time")
    private Date updatetime;

    private Integer status;
    @Column(name="id_card_id")
    private Integer idcardId;
    @Column(name="business_license_id")
    private Integer businessLicenseId;
    
    /**
     * 不通过原因
     */
    private String reason;
    
    public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getIdcardId() {
        return idcardId;
    }

    public void setIdcardId(Integer idcardId) {
        this.idcardId = idcardId;
    }

    public Integer getBusinessLicenseId() {
        return businessLicenseId;
    }

    public void setBusinessLicenseId(Integer businessLicenseId) {
        this.businessLicenseId = businessLicenseId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type == null ? null : type.trim();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name == null ? null : name.trim();
    }

    public String getIndustry() {
        return industry;
    }

    public void setIndustry(String industry) {
        this.industry = industry == null ? null : industry.trim();
    }

    public String getScale() {
        return scale;
    }

    public void setScale(String scale) {
        this.scale = scale == null ? null : scale.trim();
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce == null ? null : introduce.trim();
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address == null ? null : address.trim();
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

    public String getCreateBy() {
		return createBy;
	}

	public void setCreateBy(String createBy) {
		this.createBy = createBy;
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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}