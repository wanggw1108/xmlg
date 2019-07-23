package com;

import com.temporary.center.ls_common.HttpUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-23-18:55
 */
public class MyCollectionTest {

    //http://47.107.103.97:8081
    public static String token = "af29bea1-5446-4647-8e3f-1d9c97d7de94";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("收藏")
    public void collection(){
        String url = "http://localhost:8081/employee/collection.do";
        Map<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("recruitmentid",39);
        String str = HttpUtil.buildPostStr(params);
        try {
            String resp = HttpUtil.post(url,str,header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    @Description("查看收藏")
    public void seeCollection(){
        String url = "http://localhost:8081/employee/myCollection.do";
        HashMap<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("curr",1);
        params.put("pageSize",10);
        String str = HttpUtil.buildPostStr(params);
        try {
            String resp = HttpUtil.post(url,str,header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
