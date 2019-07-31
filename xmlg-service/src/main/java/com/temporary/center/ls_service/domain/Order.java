package com.temporary.center.ls_service.domain;

import javax.persistence.*;
import java.util.Date;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-29-9:43
 */
@Table(name = "w_order")
public class Order {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    /*订单所属雇主的id*/
    private Integer userId;
    /*关联的招聘信息id*/
    private Integer recruitId;
    /*关联的招聘信息title*/
    private String recruitTitle;
    /*订单编号*/
    private String orderId;
    /*订单状态*/
    private Integer orderState;
    /*雇佣的雇员姓名*/
    private String employeeName;
    /*雇佣的雇员ID*/
    private Integer employeeId;
    /*需要支付的金额*/
    private Float amount;
    /*开始工作日期*/
    private String startDate;
    /*结束工作日期*/
    private String endDate;
    /*总共天数*/
    private Integer totalDay;
    /*每天开始工作时间*/
    private String startTime;
    /*每天结束工作时间*/
    private String endTime;
    /*每天总共需要工作时间*/
    private Integer totalTime;
    /*需要确定开始的日期*/
    private String confirmStartDate;
    /*需要确认的结束日期*/
    private String confirmEndDate;
    /*基础工资 如：xxxx元/天*/
    private String basePay;
    /*订单取消日期*/
    private String cancelDate;
    /*订单取消原因*/
    private String cancelReason;
    /*订单完结时间*/
    private String  completeDate;
    private Date createTime;
    private Date updateTime;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getRecruitId() {
        return recruitId;
    }

    public void setRecruitId(Integer recruitId) {
        this.recruitId = recruitId;
    }

    public String getRecruitTitle() {
        return recruitTitle;
    }

    public void setRecruitTitle(String recruitTitle) {
        this.recruitTitle = recruitTitle;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public Integer getOrderState() {
        return orderState;
    }

    public void setOrderState(Integer orderState) {
        this.orderState = orderState;
    }

    public String getEmployeeName() {
        return employeeName;
    }

    public void setEmployeeName(String employeeName) {
        this.employeeName = employeeName;
    }

    public Integer getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Integer employeeId) {
        this.employeeId = employeeId;
    }

    public Float getAmount() {
        return amount;
    }

    public void setAmount(Float amount) {
        this.amount = amount;
    }

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public Integer getTotalDay() {
        return totalDay;
    }

    public void setTotalDay(Integer totalDay) {
        this.totalDay = totalDay;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Integer getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Integer totalTime) {
        this.totalTime = totalTime;
    }

    public String getConfirmStartDate() {
        return confirmStartDate;
    }

    public void setConfirmStartDate(String confirmStartDate) {
        this.confirmStartDate = confirmStartDate;
    }

    public String getConfirmEndDate() {
        return confirmEndDate;
    }

    public void setConfirmEndDate(String confirmEndDate) {
        this.confirmEndDate = confirmEndDate;
    }

    public String getBasePay() {
        return basePay;
    }

    public void setBasePay(String basePay) {
        this.basePay = basePay;
    }

    public String getCancelDate() {
        return cancelDate;
    }

    public void setCancelDate(String cancelDate) {
        this.cancelDate = cancelDate;
    }

    public String getCancelReason() {
        return cancelReason;
    }

    public void setCancelReason(String cancelReason) {
        this.cancelReason = cancelReason;
    }

    public String getCompleteDate() {
        return completeDate;
    }

    public void setCompleteDate(String completeDate) {
        this.completeDate = completeDate;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
