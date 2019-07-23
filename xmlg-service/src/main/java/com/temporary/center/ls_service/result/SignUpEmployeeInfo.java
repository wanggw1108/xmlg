package com.temporary.center.ls_service.result;

import java.util.List;

/**
 * 报名的雇员信息
 * @author  fuyuanming
 */
public class SignUpEmployeeInfo {
	
	private Integer employeeId;
	
	private Integer employeeSex;
	
	private String employeeName;
	
	private Integer employeeAge;
	
	private String employeeportraitUrl;
	
	private Integer employeeReputation;
	
	private List<String> employeeServiceDistrict;
	
	private List<String> employeeJobExperience;


	public Integer getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(Integer employeeId) {
		this.employeeId = employeeId;
	}

	public Integer getEmployeeSex() {
		return employeeSex;
	}

	public void setEmployeeSex(Integer employeeSex) {
		this.employeeSex = employeeSex;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public Integer getEmployeeAge() {
		return employeeAge;
	}

	public void setEmployeeAge(Integer employeeAge) {
		this.employeeAge = employeeAge;
	}

	public String getEmployeeportraitUrl() {
		return employeeportraitUrl;
	}

	public void setEmployeeportraitUrl(String employeeportraitUrl) {
		this.employeeportraitUrl = employeeportraitUrl;
	}

	public Integer getEmployeeReputation() {
		return employeeReputation;
	}

	public void setEmployeeReputation(Integer employeeReputation) {
		this.employeeReputation = employeeReputation;
	}

	public List<String> getEmployeeServiceDistrict() {
		return employeeServiceDistrict;
	}

	public void setEmployeeServiceDistrict(List<String> employeeServiceDistrict) {
		this.employeeServiceDistrict = employeeServiceDistrict;
	}

	public List<String> getEmployeeJobExperience() {
		return employeeJobExperience;
	}

	public void setEmployeeJobExperience(List<String> employeeJobExperience) {
		this.employeeJobExperience = employeeJobExperience;
	}
}
