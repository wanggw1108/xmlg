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
 * @create 2019-07-25-19:14
 */
public class CompanyTest {
    public static String token = "af29bea1-5446-4647-8e3f-1d9c97d7de94";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("查询公司资质")
    public void status(){
        String url = "http://localhost:8081/companyInfo/qualifications.do?";
        Map<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("companyId",6);
        String str = HttpUtil.buildPostStr(params);
        try {
            String res = HttpUtil.send(url+str);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
