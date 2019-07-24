package com.temporary.center.ls_service.domain;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-24-14:23
 */
public class InvitationMessage {

    private Integer id;
    private Integer recruitment_id;
    private String title;
    private Float base_pay;
    private String base_pay_unit;
    private String work_place;
    private String type_work;
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

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Float getBase_pay() {
        return base_pay;
    }

    public void setBase_pay(Float base_pay) {
        this.base_pay = base_pay;
    }

    public String getBase_pay_unit() {
        return base_pay_unit;
    }

    public void setBase_pay_unit(String base_pay_unit) {
        this.base_pay_unit = base_pay_unit;
    }

    public String getWork_place() {
        return work_place;
    }

    public void setWork_place(String work_place) {
        this.work_place = work_place;
    }

    public String getType_work() {
        return type_work;
    }

    public void setType_work(String type_work) {
        this.type_work = type_work;
    }
}
