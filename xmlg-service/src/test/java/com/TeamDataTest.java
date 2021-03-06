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
 * @create 2019-07-19-18:28
 */
public class TeamDataTest {

    public static String token = "e03aeb93-3544-4070-870f-affaf896e79e";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("查询认证状态")
    public void status(){
        String url = "http://localhost:8081/teamdata/status.do";
        Map<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("timeStamp",System.currentTimeMillis());
        String str = HttpUtil.buildPostStr(params);
        try {
            String res = HttpUtil.post(url,str,header);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
