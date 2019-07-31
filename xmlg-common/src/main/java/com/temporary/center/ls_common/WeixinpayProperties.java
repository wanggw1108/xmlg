package com.temporary.center.ls_common;


import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-31-17:30
 */
@Component
@ConfigurationProperties(prefix="wechat")
public class WeixinpayProperties {

    private String appid; // 公众账号ID

    private String mch_id; // 商户号

    private String device_info; // 设备号

    private String key; // 商户的key【API密匙】

    private String url; // api请求地址

    private String notify_url; // 服务器异步通知页面路径

    private String return_url; // 服务器同步通知页面路径
    private String wx_package;
    public String getAppid() {
        return appid;
    }

    public void setAppid(String appid) {
        this.appid = appid;
    }

    public String getMch_id() {
        return mch_id;
    }

    public void setMch_id(String mch_id) {
        this.mch_id = mch_id;
    }

    public String getDevice_info() {
        return device_info;
    }

    public void setDevice_info(String device_info) {
        this.device_info = device_info;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getNotify_url() {
        return notify_url;
    }

    public void setNotify_url(String notify_url) {
        this.notify_url = notify_url;
    }

    public String getReturn_url() {
        return return_url;
    }

    public void setReturn_url(String return_url) {
        this.return_url = return_url;
    }

    public String getWx_package() {
        return wx_package;
    }

    public void setWx_package(String wx_package) {
        this.wx_package = wx_package;
    }
}
