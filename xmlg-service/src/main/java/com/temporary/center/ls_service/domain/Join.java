package com.temporary.center.ls_service.domain;

import com.temporary.center.ls_service.common.Page;

import javax.persistence.*;
import java.util.Date;
@Table(name="w_join")
public class Join extends Page{

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="resume_id")
    private Integer resumeId;
    @Column(name="user_name")
    private String userName;
    @Column(name="user_id")
    private Integer userId;
    @Column(name="recruitment_info_id")
    private Integer recruitmentInfoId;
    @Column(name="recruitment_info_createBy")
    private String recruitmentInfoCreateby;
    @Column(name="recruitment_info_createBy_name")
    private String recruitmentInfoCreatebyName;
    @Column(name="create_time")
    private Date createtime;

    private String remark;

    private Integer state;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getResumeId() {
        return resumeId;
    }

    public void setResumeId(Integer resumeId) {
        this.resumeId = resumeId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRecruitmentInfoId() {
        return recruitmentInfoId;
    }

    public void setRecruitmentInfoId(Integer recruitmentInfoId) {
        this.recruitmentInfoId = recruitmentInfoId;
    }

    public String getRecruitmentInfoCreateby() {
        return recruitmentInfoCreateby;
    }

    public void setRecruitmentInfoCreateby(String recruitmentInfoCreateby) {
        this.recruitmentInfoCreateby = recruitmentInfoCreateby;
    }

    public String getRecruitmentInfoCreatebyName() {
        return recruitmentInfoCreatebyName;
    }

    public void setRecruitmentInfoCreatebyName(String recruitmentInfoCreatebyName) {
        this.recruitmentInfoCreatebyName = recruitmentInfoCreatebyName;
    }

    public Date getCreatetime() {
        return createtime;
    }

    public void setCreatetime(Date createtime) {
        this.createtime = createtime;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }
}