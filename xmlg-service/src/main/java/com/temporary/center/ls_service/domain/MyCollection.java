package com.temporary.center.ls_service.domain;


import com.temporary.center.ls_service.common.NotNullEmpty;
import com.temporary.center.ls_service.common.Page;

import java.util.Date;

public class MyCollection extends Page {
    private Long id;

    @NotNullEmpty
    private Long recruitmentid;

    @NotNullEmpty
    private String title;

    @NotNullEmpty
    private String district;

    @NotNullEmpty
    private Float basepay;

    @NotNullEmpty
    private String basepayunit;

    private Long createby;

    private Date createtime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecruitmentid() {
        return recruitmentid;
    }

    public void setRecruitmentid(Long recruitmentid) {
        this.recruitmentid = recruitmentid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title == null ? null : title.trim();
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district == null ? null : district.trim();
    }

    public Float getBasepay() {
        return basepay;
    }

    public void setBasepay(Float basepay) {
        this.basepay = basepay;
    }

    public String getBasepayunit() {
        return basepayunit;
    }

    public void setBasepayunit(String basepayunit) {
        this.basepayunit = basepayunit == null ? null : basepayunit.trim();
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
}