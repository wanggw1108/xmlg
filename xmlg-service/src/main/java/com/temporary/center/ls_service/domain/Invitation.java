package com.temporary.center.ls_service.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-24-14:01
 */
@Table(name="invitation")
public class Invitation {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    private Integer recruitment_id;
    private Integer create_by;
    private Date create_time;
    private Integer invited_curriculum_id;
    private Integer invited_user_id;
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

    public Integer getRecruitment_id() {
        return recruitment_id;
    }

    public void setRecruitment_id(Integer recruitment_id) {
        this.recruitment_id = recruitment_id;
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

    public Integer getInvited_curriculum_id() {
        return invited_curriculum_id;
    }

    public void setInvited_curriculum_id(Integer invited_curriculum_id) {
        this.invited_curriculum_id = invited_curriculum_id;
    }

    public Integer getInvited_user_id() {
        return invited_user_id;
    }

    public void setInvited_user_id(Integer invited_user_id) {
        this.invited_user_id = invited_user_id;
    }
}
