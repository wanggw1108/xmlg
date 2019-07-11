package com.temporary.center.ls_service.domain;

import javax.persistence.Column;
import javax.persistence.Table;
import java.io.Serializable;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.Date;

@Table(name = "w_user")
public class User implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = -6981955025974588141L;
	private int id; //        bigint(20)     主键
	@Column(name = "userName")
	private String userName;//    varchar(50)    用户名                         
	private String email   ;//     varchar(50)    邮箱                            
	private String phone       ;// varchar(11)    手机号                         
	private String password    ;// varchar(100)   密码
	@Column(name = "createBy")
	private String createBy    ;// varchar(20)    创建者
	@Column(name = "createTime")
	private Date createTime;// datetime       创建时间
	private String effective   ;// varchar(1)     是否有效 0:有效 1：无效
	@Column(name = "updateBy")
	private String updateBy    ;// varchar(20)    更新者
	@Column(name = "updateTime")
	private Date updateTime  ;// datetime       更新时间
	@Column(name = "qqKey")
	private String qqKey;
	@Column(name = "wxKey")
	private String wxKey;
	@Column(name = "wbKey")
	private String wbKey;
	@Column(name = "chineseName")
	private String chineseName;
	
	private Integer sex;
	
	private String city;
	
	private String birthday;
	
	private Integer height;
	
	private String qq;
	
	private String wx;
	@Column(name = "employeeReputation")
	private Integer employeeReputation;
	@Column(name = "bossReputation")
	private Integer bossReputation;
	@Column(name = "userImageUrl")
	private String userImageUrl;
	
	public String getUserImageUrl() {
		return userImageUrl;
	}
	public void setUserImageUrl(String userImageUrl) {
		this.userImageUrl = userImageUrl;
	}
	public Integer getEmployeeReputation() {
		return employeeReputation;
	}
	public void setEmployeeReputation(Integer employeeReputation) {
		this.employeeReputation = employeeReputation;
	}
	public Integer getBossReputation() {
		return bossReputation;
	}
	public void setBossReputation(Integer bossReputation) {
		this.bossReputation = bossReputation;
	}
	public String getChineseName() {
		return chineseName;
	}
	public void setChineseName(String chineseName) {
		this.chineseName = chineseName;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public Integer getHeight() {
		return height;
	}
	public void setHeight(Integer height) {
		this.height = height;
	}
	public String getQq() {
		return qq;
	}
	public void setQq(String qq) {
		this.qq = qq;
	}
	public String getWx() {
		return wx;
	}
	public void setWx(String wx) {
		this.wx = wx;
	}
	public String getQqKey() {
		return qqKey;
	}
	public void setQqKey(String qqKey) {
		this.qqKey = qqKey;
	}
	public String getWxKey() {
		return wxKey;
	}
	public void setWxKey(String wxKey) {
		this.wxKey = wxKey;
	}
	public String getWbKey() {
		return wbKey;
	}
	public void setWbKey(String wbKey) {
		this.wbKey = wbKey;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getCreateBy() {
		return createBy;
	}
	public void setCreateBy(String createBy) {
		this.createBy = createBy;
	}
	public Date getCreateTime() {
		return createTime;
	}
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}
	public String getEffective() {
		return effective;
	}
	public void setEffective(String effective) {
		this.effective = effective;
	}
	public String getUpdateBy() {
		return updateBy;
	}
	public void setUpdateBy(String updateBy) {
		this.updateBy = updateBy;
	}
	public Date getUpdateTime() {
		return updateTime;
	}
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
}
