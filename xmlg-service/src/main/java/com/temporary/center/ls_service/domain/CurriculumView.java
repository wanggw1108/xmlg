package com.temporary.center.ls_service.domain;

import java.util.List;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-25-13:24
 */
public class CurriculumView {
    private Integer curriculum_id;
    private Integer user_id;
    private String name;
    private String sex;
    private String age;
    private String userImageUrl;
    private String employeeReputation;
    private List<Locus> employeeServiceDistrict;
    private List<Locus> employeeJobExperience;

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    public Integer getCurriculum_id() {
        return curriculum_id;
    }

    public void setCurriculum_id(Integer curriculum_id) {
        this.curriculum_id = curriculum_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getUserImageUrl() {
        return userImageUrl;
    }

    public void setUserImageUrl(String userImageUrl) {
        this.userImageUrl = userImageUrl;
    }

    public String getEmployeeReputation() {
        return employeeReputation;
    }

    public void setEmployeeReputation(String employeeReputation) {
        this.employeeReputation = employeeReputation;
    }

    public List<Locus> getEmployeeServiceDistrict() {
        return employeeServiceDistrict;
    }

    public void setEmployeeServiceDistrict(List<Locus> employeeServiceDistrict) {
        this.employeeServiceDistrict = employeeServiceDistrict;
    }

    public List<Locus> getEmployeeJobExperience() {
        return employeeJobExperience;
    }

    public void setEmployeeJobExperience(List<Locus> employeeJobExperience) {
        this.employeeJobExperience = employeeJobExperience;
    }
}
