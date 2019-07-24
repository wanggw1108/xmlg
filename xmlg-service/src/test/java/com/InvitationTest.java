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
 * @create 2019-07-24-15:36
 */
public class InvitationTest {

    //http://47.107.103.97:8081
    public static String token = "af29bea1-5446-4647-8e3f-1d9c97d7de94";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("发起邀约")
    public void add(){

        String url = "http://localhost:8081/invitation/add.do";
//        header.put("Content-Type","application/json");
        try {
            Map<String,Object> params  = new HashMap<>();
            params.put("token",token);
            params.put("recruitment_id",39);
            params.put("invited_curriculum_id",9);
            params.put("msg","你好");
            String postStr=  HttpUtil.buildPostStr(params);
            String resp = HttpUtil.post(url,postStr,header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    @Description("查询邀约消息")
    public void get(){

        String url = "http://localhost:8081/invitation/getInvitation.do?";
        Map<String,Object> params = new HashMap<>();
        params.put("token","c5c0e0ff-4fb5-4c99-a06e-82b2149f5d82");
        String getStr = HttpUtil.buildPostStr(params);
        try {
            String resp = HttpUtil.send(url+getStr);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
