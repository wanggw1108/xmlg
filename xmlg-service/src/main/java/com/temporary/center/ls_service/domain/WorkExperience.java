package com.temporary.center.ls_service.domain;

import java.util.Date;

public class WorkExperience {
    private Long id;

    private Date begin;

    private String end;

    private String corporateName;

    private String companyNature;

    private String companySize;

    private String companyIndustry;

    private String jobTitle;

    private Float salary;

    private String salaryUnit;

    private String workSpace;

    private Integer subordinatesNumber;

    private String reportingObject;

    private String dutyAchievement;

    private Long createby;

    private Date createtime;

    private Long updateby;

    private Date updatetime;

    private Long curriculumViteaId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getBegin() {
        return begin;
    }

    public void setBegin(Date begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
        this.end = end == null ? null : end.trim();
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName == null ? null : corporateName.trim();
    }

    public String getCompanyNature() {
        return companyNature;
    }

    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature == null ? null : companyNature.trim();
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize == null ? null : companySize.trim();
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry == null ? null : companyIndustry.trim();
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle == null ? null : jobTitle.trim();
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getSalaryUnit() {
        return salaryUnit;
    }

    public void setSalaryUnit(String salaryUnit) {
        this.salaryUnit = salaryUnit == null ? null : salaryUnit.trim();
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace == null ? null : workSpace.trim();
    }

    public Integer getSubordinatesNumber() {
        return subordinatesNumber;
    }

    public void setSubordinatesNumber(Integer subordinatesNumber) {
        this.subordinatesNumber = subordinatesNumber;
    }

    public String getReportingObject() {
        return reportingObject;
    }

    public void setReportingObject(String reportingObject) {
        this.reportingObject = reportingObject == null ? null : reportingObject.trim();
    }

    public String getDutyAchievement() {
        return dutyAchievement;
    }

    public void setDutyAchievement(String dutyAchievement) {
        this.dutyAchievement = dutyAchievement == null ? null : dutyAchievement.trim();
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

    public Long getCurriculumViteaId() {
        return curriculumViteaId;
    }

    public void setCurriculumViteaId(Long curriculumViteaId) {
        this.curriculumViteaId = curriculumViteaId;
    }
}