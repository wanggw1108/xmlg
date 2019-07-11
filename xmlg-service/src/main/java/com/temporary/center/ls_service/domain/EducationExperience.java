package com.temporary.center.ls_service.domain;

import java.util.Date;

public class EducationExperience {
    private Long id;

    private Long curriculumViteaId;

    private Date begin;

    private Date end;

    private String majorName;

    private String cv;

    private Integer isUnified;

    private String schoolName;

    private Long createby;

    private Date createtime;

    private Long updateby;

    private Date updatetime;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurriculumViteaId() {
        return curriculumViteaId;
    }

    public void setCurriculumViteaId(Long curriculumViteaId) {
        this.curriculumViteaId = curriculumViteaId;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public Date getEnd() {
        return end;
    }

    public void setEnd(Date end) {
        this.end = end;
    }

    public String getMajorName() {
        return majorName;
    }

    public void setMajorName(String majorName) {
        this.majorName = majorName == null ? null : majorName.trim();
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv == null ? null : cv.trim();
    }

    public Integer getIsUnified() {
        return isUnified;
    }

    public void setIsUnified(Integer isUnified) {
        this.isUnified = isUnified;
    }

    public String getSchoolName() {
        return schoolName;
    }

    public void setSchoolName(String schoolName) {
        this.schoolName = schoolName == null ? null : schoolName.trim();
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
}