package com.temporary.center.ls_service.domain;

import javax.persistence.*;
import java.util.Date;
@Table(name="project_experience")
public class ProjectExperience {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    @Column(name="curriculum_vitea_id")
    private Integer curriculumViteaId;

    private String name;

    private String describe;

    private String duty;

    private String achievement;

    private Date begin;

    private Date end;
    @Column(name="create_by")
    private Integer createby;
    @Column(name="create_time")
    private Date createtime;
    @Column(name="update_by")
    private Integer updateby;
    @Column(name="update_time")
    private Date updatetime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getCurriculumViteaId() {
        return curriculumViteaId;
    }

    public void setCurriculumViteaId(Integer curriculumViteaId) {
        this.curriculumViteaId = curriculumViteaId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescribe() {
        return describe;
    }

    public void setDescribe(String describe) {
        this.describe = describe;
    }

    public String getDuty() {
        return duty;
    }

    public void setDuty(String duty) {
        this.duty = duty;
    }

    public String getAchievement() {
        return achievement;
    }

    public void setAchievement(String achievement) {
        this.achievement = achievement;
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

    public Integer getCreateby() {
        return createby;
    }

    public void setCreateby(Integer createby) {
        this.createby = createby;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public Integer getUpdateby() {
        return updateby;
    }

    public void setUpdateby(Integer updateby) {
        this.updateby = updateby;
    }

    public Date getUpdatetime() {
        return updatetime;
    }

    public void setUpdatetime(Date updatetime) {
        this.updatetime = updatetime;
    }
}