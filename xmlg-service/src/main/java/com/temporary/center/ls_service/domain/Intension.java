package com.temporary.center.ls_service.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-19-11:32
 */
@Table(name="intension")
public class Intension {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    /*用户ID*/
    private Integer user_id;
    /*工作类型*/
    private String work_type;
    /*工作区域，多个以,分割*/
    private String area;
    /*工作类别,多个以,分割*/
    private String job_type;
    /*空闲月份，多个以,分割*/
    private String free_month;
    /*全职开始日期*/
    private String all_time_start_date;
    /*全职工作时间段*/
    private String all_time_work_time;
    /*兼职空闲日期,多个以,分割*/
    private String part_date;
    /*兼职空闲时间段，多个以,分割*/
    private String part_time;
    private Date create_time;
    private Date update_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public String getWork_type() {
        return work_type;
    }

    public void setWork_type(String work_type) {
        this.work_type = work_type;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getJob_type() {
        return job_type;
    }

    public void setJob_type(String job_type) {
        this.job_type = job_type;
    }

    public String getFree_month() {
        return free_month;
    }

    public void setFree_month(String free_month) {
        this.free_month = free_month;
    }

    public String getAll_time_start_date() {
        return all_time_start_date;
    }

    public void setAll_time_start_date(String all_time_start_date) {
        this.all_time_start_date = all_time_start_date;
    }

    public String getAll_time_work_time() {
        return all_time_work_time;
    }

    public void setAll_time_work_time(String all_time_work_time) {
        this.all_time_work_time = all_time_work_time;
    }

    public String getPart_date() {
        return part_date;
    }

    public void setPart_date(String part_date) {
        this.part_date = part_date;
    }

    public String getPart_time() {
        return part_time;
    }

    public void setPart_time(String part_time) {
        this.part_time = part_time;
    }

    public Date getCreate_time() {
        return create_time;
    }

    public void setCreate_time(Date create_time) {
        this.create_time = create_time;
    }

    public Date getUpdate_time() {
        return update_time;
    }

    public void setUpdate_time(Date update_time) {
        this.update_time = update_time;
    }
}
