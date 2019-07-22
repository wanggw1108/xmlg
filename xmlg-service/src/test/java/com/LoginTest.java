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
 * @create 2019-07-22-17:36
 */
public class LoginTest {

    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }

    @Test
    @Description("登陆测试")
    public void login(){
        String url = "http://47.107.103.97:8081/user/getToken.do";
        Map<String,Object> params = new HashMap<>();
        params.put("username","18701497050");
        params.put("loginType","1");
        params.put("password","wgw1108qwe");
        String str = HttpUtil.buildPostStr(params);
        try {
            String res = HttpUtil.post(url,str,header);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
