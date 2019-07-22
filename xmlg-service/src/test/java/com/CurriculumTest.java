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
 * @create 2019-07-22-17:29
 */
public class CurriculumTest {

    //http://47.107.103.97:8081
    public static String token = "af29bea1-5446-4647-8e3f-1d9c97d7de94";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("查询简历")
    public void query(){

        String url = "http://localhost:8081/curriculum/query.do";
//        header.put("Content-Type","application/json");
        try {
            StringBuilder builder = new StringBuilder("?token=");
            builder.append(token)
                    .append("&timeStamp=")
                    .append(System.currentTimeMillis());
            String resp = HttpUtil.send(url+builder.toString());
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    @Description("更新基本信息")
    public void updateBase(){

        String url = "http://localhost:8081/curriculum/updateBase.do";
        header.put("Content-Type","application/json");
        JSONObject params = new JSONObject();
        params.put("token",token);
        params.put("id",1);
        params.put("name","张三");
//        params.put("current_company","阿里巴巴");
//        params.put("current_position","CEO");
        params.put("age",1980);
        params.put("city","杭州");
        try {
            String resp = HttpUtil.send("POST",url,params.toString(),header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Description("添加教育经历")
    public void addEdu(){

        String url = "http://localhost:8081/curriculum/addEdu.do";
        header.put("Content-Type","application/json");
        JSONObject params = new JSONObject();
        params.put("token",token);
        params.put("curriculum_vitea_id",1);
        params.put("begin","2017-01-01");
        params.put("end","2019-01-01");
        params.put("cv","本科");
        params.put("school_name","南开大学");
        params.put("major_name","计算机科学与技术");
        params.put("is_unified",1);
        params.put("msg","参加学生会工作");
        try {
            String resp = HttpUtil.send("POST",url,params.toString(),header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    @Test
    @Description("添加工作经历")
    public void addWork(){

        String url = "http://localhost:8081/curriculum/addWork.do";
        header.put("Content-Type","application/json");
        JSONObject params = new JSONObject();
        params.put("token",token);
        params.put("curriculum_vitea_id",1);
        params.put("begin","2017-01-01");
        params.put("end","2019-01-01");
        params.put("corporate_name","阿里巴巴");
        params.put("msg","搬砖");
        try {
            String resp = HttpUtil.send("POST",url,params.toString(),header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
