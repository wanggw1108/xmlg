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
 * @create 2019-07-24-23:10
 */
public class AddressTest {

    public static String token = "c5c0e0ff-4fb5-4c99-a06e-82b2149f5d82";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("查询地址列表")
    public void status(){
        String url = "http://localhost:8081/user/addressList.do?";
        Map<String,Object> params = new HashMap<>();
        params.put("token",token);
        String str = HttpUtil.buildPostStr(params);
        try {
            String res = HttpUtil.send(url+str);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
