package com.temporary.center.ls_service.domain;

import javax.persistence.*;
import java.util.Date;
@Table(name="education_experience")
public class EducationExperience {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    private Long curriculum_vitea_id;

    private Date begin;

    private Date end;
    private String major_name;

    private String cv;
    private Integer is_unified;
    private String school_name;
    private Long create_by;

    private Date create_time;

    private Long update_by;

    private Date update_time;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCurriculum_vitea_id() {
        return curriculum_vitea_id;
    }

    public void setCurriculum_vitea_id(Long curriculum_vitea_id) {
        this.curriculum_vitea_id = curriculum_vitea_id;
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

    public String getMajor_name() {
        return major_name;
    }

    public void setMajor_name(String major_name) {
        this.major_name = major_name;
    }

    public String getCv() {
        return cv;
    }

    public void setCv(String cv) {
        this.cv = cv;
    }

    public Integer getIs_unified() {
        return is_unified;
    }

    public void setIs_unified(Integer is_unified) {
        this.is_unified = is_unified;
    }

    public String getSchool_name() {
        return school_name;
    }

    public void setSchool_name(String school_name) {
        this.school_name = school_name;
    }

    public Long getCreate_by() {
        return create_by;
    }

    public void setCreate_by(Long create_by) {
        this.create_by = create_by;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Long getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(Long update_by) {
        this.update_by = update_by;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}