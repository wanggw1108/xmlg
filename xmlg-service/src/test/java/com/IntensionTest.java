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
 * @create 2019-07-19-15:07
 */
public class IntensionTest {

    public static String token = "7a64e738-b349-46f1-8cd3-64300b7b527e";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("查询求职意向")
    public void query(){
        String url = "http://47.107.103.97:8081/intension/query.do";
        StringBuilder builder = new StringBuilder("?token=");
        builder.append(token);
        try {
            String resp = HttpUtil.send(url+builder.toString());
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    @Description("更新求职意向")
    public void update(){
        String url = "http://47.107.103.97:8081/intension/update.do";
        Map<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("id",5);
        params.put("user_id",14);
        params.put("work_type","全部可预约");
        params.put("area","朝阳区,海淀区");
        params.put("job_type","保姆,家教");
        params.put("free_month","1,2,5,6");
        params.put("all_time_start_date","2019-04-01");
        params.put("all_time_work_time","8:00-18:00");
        params.put("all_time_work_time","8:00-18:00");
        params.put("part_date","2019-04-01 2019-04-18,2019-05-01 2019-05-03");
        params.put("part_time","08:00-09:00,12:00-19:00");
        String str = HttpUtil.buildPostStr(params);
        try {
            String res = HttpUtil.post(url,str,header);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
