package com.temporary.center.ls_service.domain;


import com.temporary.center.ls_service.common.NotNullEmpty;
import com.temporary.center.ls_service.common.Page;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Date;

@Table(name="collection")
public class MyCollection {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @NotNullEmpty
    private Long recruitment_id;

    @NotNullEmpty
    private String title;

    @NotNullEmpty
    private String district;

    @NotNullEmpty
    private Float base_pay;

    @NotNullEmpty
    private String base_pay_unit;

    private Long create_by;

    private Date create_time;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getRecruitment_id() {
        return recruitment_id;
    }

    public void setRecruitment_id(Long recruitment_id) {
        this.recruitment_id = recruitment_id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
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
}