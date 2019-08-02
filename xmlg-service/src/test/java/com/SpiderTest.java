package com;

import com.temporary.center.ls_common.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-25-19:36
 */
public class SpiderTest {

    public static void main(String[] args) throws IOException {

        for(int page=1;page<=10;page++){
            String url = "http://m.maozi33.pw/?m=vod-type-id-5-pg-"+page+".html";
            Map<String, String> headers = new HashMap<>();
            headers.put("User-Agent","Mozilla/5.0 (Linux; U; Android 9; zh-cn; MI 8 Build/PKQ1.180729.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.141 Mobile Safari/537.36 XiaoMi/MiuiBrowser/10.9.2");
            headers.put("Upgrade-Insecure-Requests","1");
            headers.put("x-miorigin","b");
            String resp = HttpUtil.send(url,headers);
            Document doc = Jsoup.parse(resp);
            Elements es  =doc.getElementsByClass("vbox");
            for(Element i:es){
                Element aEle = i.getElementsByTag("a").get(0);
                //详情页地址
                String u = "http://m.maozi33.pw"+aEle.attr("href");
                //标题
                String title = aEle.attr("title");
                System.out.println("标题："+title);
                //标注：例如：高清，CD等内容
                String span = aEle.child(1).text();
                System.out.println("标注："+span);
                //图片地址
                String img = aEle.child(0).attr("data-original");
                System.out.println("图片地址："+img);
                //进入详情页
                String detailPage = HttpUtil.send(u,headers);
                Document docDetail = Jsoup.parse(detailPage);
                //导演等内容，
                String dbox = docDetail.getElementsByClass("data").get(0).text();
                //播放地址
                Elements tbox_tabs = docDetail.getElementsByClass("list_block show").get(0).getElementsByTag("a");
                List<String> list = new ArrayList<String>();
                for(Element t:tbox_tabs){
                    list.add(t.text()+"|"+"http://m.maozi33.pw"+t.attr("href"));
                }
                //剧情介绍
                String tbox_js = docDetail.getElementsByClass("tbox_js").get(0).html();
                System.out.println("导演等内容：");
                System.out.println(dbox);
                System.out.println("播放地址：");
                System.out.println(list);
                System.out.println("剧情介绍：");
                System.out.println(tbox_js);
                System.out.println("================================");
            }
        }

    }

}
