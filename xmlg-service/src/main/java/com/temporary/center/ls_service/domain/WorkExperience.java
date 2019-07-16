package com.temporary.center.ls_service.domain;

import javax.persistence.*;
import java.util.Date;
@Table(name="work_experience")
public class WorkExperience {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Date begin;
    private String end;
    @Column(name="corporate_name")
    private String corporateName;
    @Column(name="company_nature")
    private String companyNature;
    @Column(name="company_size")
    private String companySize;
    @Column(name="company_industry")
    private String companyIndustry;
    @Column(name="job_title")
    private String jobTitle;
    private Float salary;
    @Column(name="salary_unit")
    private String salaryUnit;
    @Column(name="work_space")
    private String workSpace;
    @Column(name="subordinates_number")
    private Integer subordinatesNumber;
    @Column(name="reporting_object")
    private String reportingObject;
    @Column(name="duty_achievement")
    private String dutyAchievement;
    @Column(name="create_by")
    private Long createby;
    @Column(name="create_time")
    private Date createtime;
    @Column(name="update_by")
    private Long updateby;
    @Column(name="update_time")
    private Date updatetime;
    @Column(name="curriculum_vitea_id")
    private Long curriculumViteaId;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
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
        this.end = end;
    }

    public String getCorporateName() {
        return corporateName;
    }

    public void setCorporateName(String corporateName) {
        this.corporateName = corporateName;
    }

    public String getCompanyNature() {
        return companyNature;
    }

    public void setCompanyNature(String companyNature) {
        this.companyNature = companyNature;
    }

    public String getCompanySize() {
        return companySize;
    }

    public void setCompanySize(String companySize) {
        this.companySize = companySize;
    }

    public String getCompanyIndustry() {
        return companyIndustry;
    }

    public void setCompanyIndustry(String companyIndustry) {
        this.companyIndustry = companyIndustry;
    }

    public String getJobTitle() {
        return jobTitle;
    }

    public void setJobTitle(String jobTitle) {
        this.jobTitle = jobTitle;
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
        this.salaryUnit = salaryUnit;
    }

    public String getWorkSpace() {
        return workSpace;
    }

    public void setWorkSpace(String workSpace) {
        this.workSpace = workSpace;
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
        this.reportingObject = reportingObject;
    }

    public String getDutyAchievement() {
        return dutyAchievement;
    }

    public void setDutyAchievement(String dutyAchievement) {
        this.dutyAchievement = dutyAchievement;
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