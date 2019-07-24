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

    public static String token = "7a64e738-b349-46f1-8cd3-64300b7b527e";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("查询认证状态")
    public void status(){
        String url = "http://47.107.103.97:8081/teamdata/status.do";
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
