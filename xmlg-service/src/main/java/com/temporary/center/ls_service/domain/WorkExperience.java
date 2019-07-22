package com.temporary.center.ls_service.domain;

import com.temporary.center.ls_service.common.NotNullEmpty;

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
    private String corporate_name;
    @Column(name="company_nature")
    private String company_nature;
    @Column(name="company_size")
    private String company_size;
    @Column(name="company_industry")
    private String company_industry;
    @Column(name="job_title")
    private String job_title;
    private Float salary;
    @Column(name="salary_unit")
    private String salary_unit;
    @Column(name="work_space")
    private String work_space;
    @Column(name="subordinates_number")
    private Integer subordinates_number;
    @Column(name="reporting_object")
    private String reporting_object;
    @Column(name="duty_achievement")
    private String duty_achievement;
    @Column(name="create_by")
    private Integer create_by;
    @Column(name="create_time")
    private Date create_time;
    @Column(name="update_by")
    private Integer update_by;
    @Column(name="update_time")
    private Date update_time;
    @NotNullEmpty
    private Integer curriculum_vitea_id;

    private String msg;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

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

    public String getCorporate_name() {
        return corporate_name;
    }

    public void setCorporate_name(String corporate_name) {
        this.corporate_name = corporate_name;
    }

    public String getCompany_nature() {
        return company_nature;
    }

    public void setCompany_nature(String company_nature) {
        this.company_nature = company_nature;
    }

    public String getCompany_size() {
        return company_size;
    }

    public void setCompany_size(String company_size) {
        this.company_size = company_size;
    }

    public String getCompany_industry() {
        return company_industry;
    }

    public void setCompany_industry(String company_industry) {
        this.company_industry = company_industry;
    }

    public String getJob_title() {
        return job_title;
    }

    public void setJob_title(String job_title) {
        this.job_title = job_title;
    }

    public Float getSalary() {
        return salary;
    }

    public void setSalary(Float salary) {
        this.salary = salary;
    }

    public String getSalary_unit() {
        return salary_unit;
    }

    public void setSalary_unit(String salary_unit) {
        this.salary_unit = salary_unit;
    }

    public String getWork_space() {
        return work_space;
    }

    public void setWork_space(String work_space) {
        this.work_space = work_space;
    }

    public Integer getSubordinates_number() {
        return subordinates_number;
    }

    public void setSubordinates_number(Integer subordinates_number) {
        this.subordinates_number = subordinates_number;
    }

    public String getReporting_object() {
        return reporting_object;
    }

    public void setReporting_object(String reporting_object) {
        this.reporting_object = reporting_object;
    }

    public String getDuty_achievement() {
        return duty_achievement;
    }

    public void setDuty_achievement(String duty_achievement) {
        this.duty_achievement = duty_achievement;
    }

    public Integer getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Integer create_by) {
        this.create_by = create_by;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Integer getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(Integer update_by) {
        this.update_by = update_by;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }

    public Integer getCurriculum_vitea_id() {
        return curriculum_vitea_id;
    }

    public void setCurriculum_vitea_id(Integer curriculum_vitea_id) {
        this.curriculum_vitea_id = curriculum_vitea_id;
    }
}