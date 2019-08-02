package com;

import com.temporary.center.ls_common.HttpUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author wangguowei
 * @description
 * @create 2019-08-02-13:40
 */
public class SpiderTest {

    public static void main(String[] args) throws IOException {
        String url = "http://m.maozi33.pw/?m=vod-type-id-5.html";
        Map<String, String> headers = new HashMap<>();
        headers.put("User-Agent","Mozilla/5.0 (Linux; U; Android 9; zh-cn; MI 8 Build/PKQ1.180729.001) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/71.0.3578.141 Mobile Safari/537.36 XiaoMi/MiuiBrowser/10.9.2");
        headers.put("Upgrade-Insecure-Requests","1");
        headers.put("x-miorigin","b");
        String resp = HttpUtil.send(url,headers);
        System.out.println(resp);
        Document doc = Jsoup.parse(resp);
        Elements es  =doc.getElementsByClass("vbox");
        for(Element i:es){
            Element aEle = i.getElementsByTag("a").get(0);
            String u = "http://m.maozi33.pw"+aEle.attr("href");
            System.out.println(u);
            String title = aEle.attr("title");
            String span = aEle.child(1).text();
            String img = aEle.child(0).attr("data-original");
            String detailPage = HttpUtil.send(u,headers);

            System.out.println(title);
            System.out.println(span);
            System.out.println(img);
            System.out.println("================================");
            System.out.println(detailPage);
            System.exit(1);
            Document docDetail = Jsoup.parse(detailPage);
            String dbox = docDetail.getElementsByClass("dbox").get(0).text();
            String tbox_tabs = docDetail.getElementsByClass("dbox").get(0).text();
        }
    }


}
