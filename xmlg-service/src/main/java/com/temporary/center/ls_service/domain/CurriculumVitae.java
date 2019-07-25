package com.temporary.center.ls_service.domain;

import com.temporary.center.ls_service.common.NotNullEmpty;

import javax.persistence.*;
import java.util.Date;

@Table(name="curriculum_vitae")
public class CurriculumVitae {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @NotNullEmpty
    private Integer id;
    private String name;
    private String sex;
    private String current_company;
    private String current_position;
    private String working_life;
    private Integer age;
    private String tel;
    private String nationality;
    private Integer status;
    private String household_register;
    private String email;
    private String city;
    private Float height;
    private String expect_type;
    private String expect_time;
    private String ok_work_time;
    private String full_time;
    private String self_evaluation;
    private String expect_industry;
    private String expect_position;
    private String expect_city;
    private String expect_salary;
    private String language;
    private String additional;
    private Integer create_by;
    private Date create_time;

    private Integer update_by;

    private Date update_time;

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCurrent_company() {
        return current_company;
    }

    public void setCurrent_company(String current_company) {
        this.current_company = current_company;
    }

    public String getCurrent_position() {
        return current_position;
    }

    public void setCurrent_position(String current_position) {
        this.current_position = current_position;
    }

    public String getWorking_life() {
        return working_life;
    }

    public void setWorking_life(String working_life) {
        this.working_life = working_life;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getNationality() {
        return nationality;
    }

    public void setNationality(String nationality) {
        this.nationality = nationality;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public String getHousehold_register() {
        return household_register;
    }

    public void setHousehold_register(String household_register) {
        this.household_register = household_register;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Float getHeight() {
        return height;
    }

    public void setHeight(Float height) {
        this.height = height;
    }

    public String getExpect_type() {
        return expect_type;
    }

    public void setExpect_type(String expect_type) {
        this.expect_type = expect_type;
    }

    public String getExpect_time() {
        return expect_time;
    }

    public void setExpect_time(String expect_time) {
        this.expect_time = expect_time;
    }

    public String getOk_work_time() {
        return ok_work_time;
    }

    public void setOk_work_time(String ok_work_time) {
        this.ok_work_time = ok_work_time;
    }

    public String getFull_time() {
        return full_time;
    }

    public void setFull_time(String full_time) {
        this.full_time = full_time;
    }

    public String getSelf_evaluation() {
        return self_evaluation;
    }

    public void setSelf_evaluation(String self_evaluation) {
        this.self_evaluation = self_evaluation;
    }

    public String getExpect_industry() {
        return expect_industry;
    }

    public void setExpect_industry(String expect_industry) {
        this.expect_industry = expect_industry;
    }

    public String getExpect_position() {
        return expect_position;
    }

    public void setExpect_position(String expect_position) {
        this.expect_position = expect_position;
    }

    public String getExpect_city() {
        return expect_city;
    }

    public void setExpect_city(String expect_city) {
        this.expect_city = expect_city;
    }

    public String getExpect_salary() {
        return expect_salary;
    }

    public void setExpect_salary(String expect_salary) {
        this.expect_salary = expect_salary;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getAdditional() {
        return additional;
    }

    public void setAdditional(String additional) {
        this.additional = additional;
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