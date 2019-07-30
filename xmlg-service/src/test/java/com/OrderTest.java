package com;

import com.temporary.center.ls_common.HttpUtil;
import org.json.HTTP;
import org.junit.Test;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-30-18:23
 */
public class OrderTest {
    //http://47.107.103.97:8081
    public static String token = "4ccf0e5b-853f-4a12-9476-3deeb2353501";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("聘用")
    public void create(){

        String url = "http://localhost:8081/order/create.do?";
//        header.put("Content-Type","application/json");
        try {
            HashMap<String,Object> params = new HashMap<>();
            params.put("token",token);
            params.put("recruitId",39);
            params.put("employeeId",14);
            String resp = HttpUtil.send(url+ HttpUtil.buildPostStr(params));
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
