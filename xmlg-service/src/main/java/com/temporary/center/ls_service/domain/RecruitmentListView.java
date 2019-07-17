package com.temporary.center.ls_service.domain;

import com.alibaba.fastjson.annotation.JSONField;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-17-9:36
 */
public class RecruitmentListView {

    private int id;
    private int recruitmentCategory;
    private String createSex;
    @JSONField(name = "userImageUrl")
    private String publisherPortraitUrl;
    private String bossReputation;
    private String createtime;
    private int browseTime;
    @JSONField(name="number")
    private int needNumber=0;
    @JSONField(name="recruitment")
    private int recruitNumber=0;
    @JSONField(name = "district")
    private String workDistrict;
    private int distance;
    private int basePay;
    private String basePayUnit;
    private String welfare;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRecruitmentCategory() {
        return recruitmentCategory;
    }

    public void setRecruitmentCategory(int recruitmentCategory) {
        this.recruitmentCategory = recruitmentCategory;
    }

    public int getNeedNumber() {
        return needNumber;
    }

    public void setNeedNumber(int needNumber) {
        this.needNumber = needNumber;
    }

    public int getRecruitNumber() {
        return recruitNumber;
    }

    public void setRecruitNumber(int recruitNumber) {
        this.recruitNumber = recruitNumber;
    }

    public String getWorkDistrict() {
        return workDistrict;
    }

    public void setWorkDistrict(String workDistrict) {
        this.workDistrict = workDistrict;
    }

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public int getBasePay() {
        return basePay;
    }

    public void setBasePay(int basePay) {
        this.basePay = basePay;
    }

    public String getCreateSex() {
        return createSex;
    }

    public void setCreateSex(String createSex) {
        this.createSex = createSex;
    }

    public String getPublisherPortraitUrl() {
        return publisherPortraitUrl;
    }

    public void setPublisherPortraitUrl(String publisherPortraitUrl) {
        this.publisherPortraitUrl = publisherPortraitUrl;
    }

    public String getBossReputation() {
        return bossReputation;
    }

    public void setBossReputation(String bossReputation) {
        this.bossReputation = bossReputation;
    }

    public String getCreatetime() {
        return createtime;
    }

    public void setCreatetime(String createtime) {
        this.createtime = createtime;
    }

    public int getBrowseTime() {
        return browseTime;
    }

    public void setBrowseTime(int browseTime) {
        this.browseTime = browseTime;
    }



    public String getBasePayUnit() {
        return basePayUnit;
    }

    public void setBasePayUnit(String basePayUnit) {
        this.basePayUnit = basePayUnit;
    }

    public String getWelfare() {
        return welfare;
    }

    public void setWelfare(String welfare) {
        this.welfare = welfare;
    }
}
