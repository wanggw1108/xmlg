package com.temporary.center.ls_service.domain;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author wangguowei
 * @description
 * @create 2019-08-02-17:01
 */
@Table(name="video_detail")
public class VideoDetail {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;
    private Integer v_id;
    private String videoIndex;
    private Integer videoSort;
    private String videoUrl;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getV_id() {
        return v_id;
    }

    public void setV_id(Integer v_id) {
        this.v_id = v_id;
    }

    public String getVideoIndex() {
        return videoIndex;
    }

    public void setVideoIndex(String videoIndex) {
        this.videoIndex = videoIndex;
    }

    public Integer getVideoSort() {
        return videoSort;
    }

    public void setVideoSort(Integer videoSort) {
        this.videoSort = videoSort;
    }

    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }
}
