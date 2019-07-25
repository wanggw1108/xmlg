package com;

import com.temporary.center.ls_common.HttpUtil;
import org.junit.Test;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-24-17:52
 */
public class IdCardTest {

    //http://47.107.103.97:8081
    public static String token = "af29bea1-5446-4647-8e3f-1d9c97d7de94";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("身份证认证")
    public void auth(){
        /**
         * fileName1，fileName2 这两个参数，先调用/file/upload.do接口，获取到上传图片后的地址。
         */
        String url = "http://localhost:8081/idCard/add.do";
        Map<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("managerName","王某");
        params.put("managerId","3243028402384302");
        params.put("timeStamp","1231212");
        String str = HttpUtil.buildPostStr(params);
        try {
            String resp = HttpUtil.post(url,str,header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
