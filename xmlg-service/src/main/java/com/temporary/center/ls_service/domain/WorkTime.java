package com.temporary.center.ls_service.domain;

import java.util.Date;

public class WorkTime {
    private Long id;

    private Long recruitmentInfoId;

    private Date startTime;

    private Date endTime;

    private Integer sort;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getRecruitmentInfoId() {
        return recruitmentInfoId;
    }

    public void setRecruitmentInfoId(Long recruitmentInfoId) {
        this.recruitmentInfoId = recruitmentInfoId;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}