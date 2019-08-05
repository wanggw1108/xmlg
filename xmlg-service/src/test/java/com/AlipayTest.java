package com;

import com.temporary.center.ls_common.HttpUtil;
import org.junit.Test;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author wangguowei
 * @description
 * @create 2019-08-05-16:35
 */
public class AlipayTest {


    //http://47.107.103.97:8081
    public static String token = "4ccf0e5b-853f-4a12-9476-3deeb2353501";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("支付宝转账")
    public void pay(){

        String url = "http://localhost:8081/payback/transfer.do?";
//        header.put("Content-Type","application/json");
        try {
            HashMap<String,Object> params = new HashMap<>();
            params.put("token",token);
            params.put("amount",0.01);
            params.put("account","1069796416@qq.com");
            params.put("realName","王国伟");
            String resp = HttpUtil.send(url+HttpUtil.buildPostStr(params));
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }



}
