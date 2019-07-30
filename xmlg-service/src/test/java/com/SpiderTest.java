package com;

import com.temporary.center.ls_common.HttpUtil;

import java.io.IOException;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-25-19:36
 */
public class SpiderTest {

    public static void main(String[] args){

        String url = "https://s.taobao.com/search?q=9.9%E5%85%83%E5%8C%85%E9%82%AE&imgfile=&js=1&stats_click=search_radio_all%3A1&initiative_id=staobaoz_20190725&ie=utf8&bcoffset=-3&ntoffset=-3&p4ppushleft=1%2C48&s=4488";
        try {
            String html = HttpUtil.send(url);
            System.out.println(html);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
