package com.temporary.center.ls_common;

import com.alibaba.fastjson.JSONPath;
import com.baidu.aip.ocr.AipOcr;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-17-15:53
 */
@Component
public class AipOcrUtil {

    Logger logger = LoggerFactory.getLogger(getClass());
    @Value("${baidu_appId}")
    private  final String baidu_appId = "10519480";
    @Value("${baidu_aipKey}")
    private  final String baidu_aipKey = "TGcD6lrn6KbGSB6loKVpcQxx";
    @Value("${baidu_aipToken}")
    private  String baidu_aipToken = "YiTmqo9htgAFuA6uND6n6b9iZCVGG1Qa";

    public JSONObject icardOCR(String fileName) {
        AipOcr aipOcr = new AipOcr(this.baidu_appId, this.baidu_aipKey, this.baidu_aipToken);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        options.put("detect_direction", "true");
        options.put("detect_risk", "false");
        // 参数为本地路径
        JSONObject res = aipOcr.idcard(fileName, true, options);
        System.out.println(res.toString(2));
        return res;
    }
    public JSONObject businessLicense(String fileName) {
        AipOcr aipOcr = new AipOcr(this.baidu_appId, this.baidu_aipKey, this.baidu_aipToken);
        // 传入可选参数调用接口
        HashMap<String, String> options = new HashMap<String, String>();
        // 参数为本地路径
        JSONObject res = aipOcr.businessLicense(fileName, options);
        System.out.println(res.toString(2));
        return res;
    }

    public static void main(String[] args){

        JSONObject json = new AipOcrUtil().businessLicense("E:/images/营业执照.jpg");
    }



}
