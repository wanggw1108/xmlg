package com;

import com.temporary.center.ls_common.HttpUtil;
import org.junit.Test;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-31-16:31
 */
public class WeChatPayTest {

    //http://47.107.103.97:8081
    public static String token = "4ccf0e5b-853f-4a12-9476-3deeb2353501";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("微信支付")
    public void pay(){

        String url = "http://localhost:8081/wechat/pay.do";
//        header.put("Content-Type","application/json");
        try {
            HashMap<String,Object> params = new HashMap<>();
            params.put("token",token);
            params.put("recruitId",39);
            params.put("employeeId",14);
            String resp = HttpUtil.post(url, HttpUtil.buildPostStr(params),header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    @Description("支付宝支付")
    public void alipay(){

        String url = "http://localhost:8081/alipay/pay.do";
//        header.put("Content-Type","application/json");
        try {
            HashMap<String,Object> params = new HashMap<>();
            params.put("token",token);
            params.put("recruitId",39);
            params.put("employeeId",14);
            String resp = HttpUtil.post(url, HttpUtil.buildPostStr(params),header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
