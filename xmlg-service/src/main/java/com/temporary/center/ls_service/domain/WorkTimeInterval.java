package com.temporary.center.ls_service.domain;

import javax.persistence.*;

@Table(name="work_time_interval")
public class WorkTimeInterval {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name="recruitment_info_id")
    private Integer recruitmentInfoId;
    @Column(name="start_hour")
    private Integer startHour;
    @Column(name="start_minute")
    private Integer startMinute;
    @Column(name="end_hour")
    private Integer endHour;
    @Column(name="end_minute")
    private Integer endMinute;

    private Integer sort;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getRecruitmentInfoId() {
        return recruitmentInfoId;
    }

    public void setRecruitmentInfoId(Integer recruitmentInfoId) {
        this.recruitmentInfoId = recruitmentInfoId;
    }

    public Integer getStartHour() {
        return startHour;
    }

    public void setStartHour(Integer startHour) {
        this.startHour = startHour;
    }

    public Integer getStartMinute() {
        return startMinute;
    }

    public void setStartMinute(Integer startMinute) {
        this.startMinute = startMinute;
    }

    public Integer getEndHour() {
        return endHour;
    }

    public void setEndHour(Integer endHour) {
        this.endHour = endHour;
    }

    public Integer getEndMinute() {
        return endMinute;
    }

    public void setEndMinute(Integer endMinute) {
        this.endMinute = endMinute;
    }

    public Integer getSort() {
        return sort;
    }

    public void setSort(Integer sort) {
        this.sort = sort;
    }
}