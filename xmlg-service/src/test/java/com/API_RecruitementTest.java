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
 * @create 2019-07-16-17:33
 */
public class API_RecruitementTest{
//http://47.107.103.97:8081
    public static String token = "7a64e738-b349-46f1-8cd3-64300b7b527e";
    public static HashMap<String,String> header = new HashMap<>();
    {
        header.put("Content-Type","application/x-www-form-urlencoded");
    }
    @Test
    @Description("发布职位测试")
    public void addRecruitment(){
        header.put("Content-Type","application/json");
        String url = "http://localhost:8081/recruitment/add.do";
        JSONObject params = new JSONObject();
        params.put("token",token);
        params.put("timeStamp",System.currentTimeMillis());
        params.put("loginType",1);
        params.put("title","超市招聘兼职");
        params.put("typeWork",17);
        params.put("number",20);
        params.put("settlementMethod",1);
        params.put("basePay",400);
        params.put("basePayUnit",201);
        params.put("type",1);
        params.put("sex",2);
        params.put("welfare","600,601,602");
        params.put("workingStartTime","2019-09-01,2019-09-15");
        params.put("workingEndTime","2019-09-05,2019-09-18");
        params.put("workingTime","09:00-12:00,02:00-06:00");
        params.put("workPlace","北京市朝阳区");
        params.put("longitude","116.492806");
        params.put("latitude","39.931535");
        params.put("latitude","39.931535");
        params.put("detail","   负责酒品销售工作" +
                "要求对工作认真负责。");
        params.put("recruitmentStartTime","2019-08-12 00:00:00");
        params.put("recruitmentEndTime","2019-10-12 00:00:00");
        params.put("contactsName","王阳明");
        params.put("tel","17111111111");
        params.put("email","121@qq.com");
        params.put("district","北京市");
        params.put("recruitmentCategory","2");


        try {
            String resp = HttpUtil.send("POST",url,params.toString(),header);
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Description("查看职位详情测试")
    public void detail(){
        String url = "http://localhost:8081/recruitment/listPage.do";
        StringBuilder builder = new StringBuilder("?id=");
        builder.append(33)
                .append("&token=")
                .append(token);
        try {
            String resp = HttpUtil.send(url+builder.toString());
            System.out.println(resp);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Test
    @Description("查询职位列表测试")
    public void list(){
        String url = "http://localhost:8081/recruitment/list.do?";
        Map<String,Object> params = new HashMap<>();
        params.put("district","北京市");
        params.put("search_text","兼职");
        params.put("longitude","116.492806");
        params.put("latitude","39.931535");
        params.put("latitude","39.931535");
        params.put("curr","1");
        params.put("pageSize","16");
        params.put("sort","1");
//        params.put("sortRule","asc");
        String str = HttpUtil.buildPostStr(params);
        try {
            String res = HttpUtil.send(url+str);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Test
    @Description("公司认证,调用此接口前，先调用文件上传接口，将图片上传")
    public void companyAuthTest(){
        String url = "http://localhost:8081/companyInfo/createAuthInfo.do";
        Map<String,Object> params = new HashMap<>();
        params.put("token",token);
        params.put("fileName","http://47.107.103.97/images/user/14/business/header_company.png");
        params.put("timeStamp",System.currentTimeMillis());
        params.put("companyName","测试公司14");
        params.put("companyCode","测试code1234");
        params.put("companyName","测试公司14");
        params.put("companyIndustry","28");//公司行业，见字典表
        params.put("companyScale","79");//公司规模，见字典表
        params.put("companyAddress","北京市朝阳区孟婆桥");
        params.put("companyNumber","010-99199191");
        params.put("companyIntroduction","职业咨询，家政咨询");
        params.put("companyType","0");//公司类型，0公司，1团队
        params.put("qualificationsOneUrl","http://47.107.103.97/images/user/14/business/qualification1.png");
        params.put("longitude","116.510053");
        params.put("latitude","39.943485");
        String str = HttpUtil.buildPostStr(params);
        try {
            String res = HttpUtil.post(url,str,header);
            System.out.println(res);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
