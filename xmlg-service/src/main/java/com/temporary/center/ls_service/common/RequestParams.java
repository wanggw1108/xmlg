package com.temporary.center.ls_service.common;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-15-9:43
 */
public class RequestParams<T> {

    private String token;
    private String sign;
    private long timeStamp;
    private T params;

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    public long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public T getParams() {
        return params;
    }

    public void setParams(T params) {
        this.params = params;
    }
}
