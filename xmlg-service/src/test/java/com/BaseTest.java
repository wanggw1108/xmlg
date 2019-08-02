package com;

import app.MyApplication;
import com.github.pagehelper.PageHelper;
import com.temporary.center.ls_common.HttpUtil;
import com.temporary.center.ls_common.MD5Utils;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_service.dao.*;
import com.temporary.center.ls_service.domain.*;
import com.temporary.center.ls_service.service.RecruitmentService;
import net.minidev.json.JSONArray;
import org.apache.commons.collections.map.HashedMap;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import tk.mybatis.mapper.entity.Example;

import java.io.IOException;
import java.util.*;

/**
 * @author wangguowei
 * @description
 * @create 2019-07-12-10:50
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = MyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class BaseTest {

    @Autowired
    VideoMapper videoMapper;
    @Autowired
    VideoDetailMapper videoDetailMapper;
    @Test
    public void dbTest() throws IOException {
        //采集10页数据
        for(int page=10;page<=1;page++){
            String url = "http://m.maozi33.pw/?m=vod-type-id-2-pg-"+page+".html";
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
                String dbox = docDetail.getElementsByClass("data").get(0).html();
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

                Video video = new Video();
                video.setUrl(u);
                int count = videoMapper.selectCount(video);
                if(count==0){
                    //无重复，入库
                    Video new_video = new Video();
                    new_video.setUrl(u);
                    new_video.setCreateBy(dbox);
                    new_video.setDetail(tbox_js);
                    new_video.setImg(img);
                    new_video.setTitle(title);
                    new_video.setTag(span);
                    new_video.setType("电视剧");
                    new_video.setUpdateTime(new Date());
                    new_video.setCreateTime(new Date());
                    videoMapper.insert(new_video);
                    System.out.println("新的video入库："+new_video.getTitle());
                    for(int j=0;j<list.size();j++){
                        String[] ss = list.get(j).split("\\|");
                        VideoDetail detail = new VideoDetail();
                        detail.setV_id(new_video.getId());
                        detail.setVideoIndex(ss[0]);
                        detail.setVideoUrl(ss[1]);
                        detail.setVideoSort(j);
                        videoDetailMapper.insert(detail);
                        System.out.println("新的vidoDetail入库："+detail.getVideoIndex());
                    }
                }else {
                    Video v = new Video();
                    v.setUrl(u);
                    v = videoMapper.selectOne(v);

                    if(span!=null &&span!=""){
                        if(!span.equals(v.getTag())){
                            v.setTag(span);
                            v.setUpdateTime(new Date());
                            videoMapper.updateByPrimaryKey(v);
                            System.out.println("更新video："+v.getTitle());
                        }
                    }
                    VideoDetail vd = new VideoDetail();
                    vd.setV_id(v.getId());
                    int detailCount = videoDetailMapper.selectCount(vd);
                    if(detailCount<list.size()){
                        for(int index=list.size()-detailCount;index<list.size();index++){
                            String[] ss = list.get(index).split("\\|");
                            VideoDetail detail = new VideoDetail();
                            detail.setV_id(v.getId());
                            detail.setVideoIndex(ss[0]);
                            detail.setVideoUrl(ss[1]);
                            detail.setVideoSort(index);
                            videoDetailMapper.insert(detail);
                            System.out.println("新的vidoDetail入库："+detail.getVideoIndex());
                        }
                    }


                }

            }
        }


    }

}
