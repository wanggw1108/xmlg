package com.temporary.center.ls_service.domain;

import com.temporary.center.ls_service.common.NotNullEmpty;

import javax.persistence.*;
import java.util.Date;
@Table(name="education_experience")
public class EducationExperience {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @NotNullEmpty
    private Integer curriculum_vitea_id;

    private String begin;

    private String end;
    private String major_name;

    private String cv;
    private Integer is_unified;
    private String school_name;
    private Integer create_by;

    private Date create_time;

    private Integer update_by;

    private Date update_time;
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


    public String getBegin() {
        return begin;
    }

    public void setBegin(String begin) {
        this.begin = begin;
    }

    public String getEnd() {
        return end;
    }

    public void setEnd(String end) {
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

    public Integer getCurriculum_vitea_id() {
        return curriculum_vitea_id;
    }

    public void setCurriculum_vitea_id(Integer curriculum_vitea_id) {
        this.curriculum_vitea_id = curriculum_vitea_id;
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
}