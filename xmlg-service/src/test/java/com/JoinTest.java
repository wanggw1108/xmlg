package com;

import com.temporary.center.ls_common.HttpUtil;
import net.sf.json.JSONObject;
import org.junit.Test;
import org.springframework.context.annotation.Description;

import java.io.IOException;
import java.util.HashMap;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-23-16:59
 */
public class JoinTest {

    //http://47.107.103.97:8081
    public static String token = "4ccf0e5b-853f-4a12-9476-3deeb2353501";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("报名")
    public void join(){
        String url = "http://localhost:8081/joinController/join.do";
        header.put("Content-Type","application/json");
        JSONObject params = new JSONObject();
        params.put("token",token);
        params.put("recruitId",39);
        params.put("remark","工作能力突出，吃苦耐劳");
        params.put("startDate","2019-07-25");
        params.put("endDate","2019-07-29");
        try {
            String resp = HttpUtil.send("POST",url,params.toString(),header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    @Description("查看职位下的报名信息")
    public void seeJoin(){
        String url = "http://localhost:8081/joinController/seeSignUp.do?";
        HashMap<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("recruitId",39);
        params.put("curr",1);
        params.put("pageSize",10);
        String str = HttpUtil.buildPostStr(params);
        try {
            String resp = HttpUtil.send(url+str);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
