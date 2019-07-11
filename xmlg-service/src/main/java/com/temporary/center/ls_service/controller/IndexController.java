package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.domain.CarouselPicture;
import com.temporary.center.ls_service.domain.Picture;
import com.temporary.center.ls_service.service.CarouselPictureService;
import com.temporary.center.ls_service.service.PictureService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.*;

/**
 * 主页面-首页
 * @author  fuyuanming
 *
 */
@Controller
@RequestMapping(value = "/index")
public class IndexController {
	
	private static final LogUtil logger = LogUtil.getLogUtil(IndexController.class);

	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private CarouselPictureService carouselPictureService;
	
	@Autowired
	private PictureService pictureService;
	
	
	/**
	 * 首页四张轮播图片
	 * @return
	 */
	@RequestMapping(value = "/carouselPicture.do", method = RequestMethod.POST)
    @ResponseBody
	public Json carouselPicture() {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="获取首页四张轮播图片,"+uuid;
		logger.info(title+",carouselPicture"+Constant.METHOD_BEGIN);
		
		Json json=new Json ();
		json.setFail();
		
		try {
			
			if(redisBean.exists(RedisKey.CAROUSEL_PICTURE)) {
				List<String> carouselPictureList = redisBean.lRange(RedisKey.CAROUSEL_PICTURE, 0L, -1L);
				logger.info("读取redis数据，key="+RedisKey.CAROUSEL_PICTURE+",value="+carouselPictureList);
				List<Map<String,String>> urlList=new ArrayList<Map<String,String>>();
				for(String picList:carouselPictureList) {
					String[] picArr = picList.split("");	
					if(null!=picArr && picArr.length==3) {
						String img = picArr[0];
						String pageUrl = picArr[1];
						String bgImg = picArr[2];
						Map<String, String> resultMap=new HashMap<String, String>();
						resultMap.put("img", img);
						resultMap.put("pageUrl", pageUrl);
						resultMap.put("bgImg", bgImg);
						urlList.add(resultMap);
					}else {
						logger.error("redis数据有误，key="+RedisKey.CAROUSEL_PICTURE);
					}
				}
				
				json.setData(carouselPictureList);
				json.setSuc();
			}else {
				List<CarouselPicture> carouselPictureList=carouselPictureService.getALL();
				if(null==carouselPictureList || carouselPictureList.size()==0) {
					logger.info("数据库无数据，请添加轮播图片数据");
					json.setMsg("数据库无数据，请添加轮播图片数据");
					json.setSattusCode(StatusCode.NO_DATA);
				}else {
					List<Map<String,String>> urlList=new ArrayList<Map<String,String>>();
					Map<String,String> urlMap=new HashMap<String,String>();
					int redisIndex=0;
					for(int i=carouselPictureList.size()-1;i>=0;i--) {
						CarouselPicture carouselPicture=carouselPictureList.get(i);
						String url = carouselPicture.getUrl();
						String bgImg = carouselPicture.getBgImg();
						String pageUrl = carouselPicture.getPageUrl();
						
						CarouselPicture carouselPicture_Redis=carouselPictureList.get(redisIndex);
						redisBean.lpush(RedisKey.CAROUSEL_PICTURE, carouselPicture_Redis.getUrl()+"#"+carouselPicture_Redis.getPageUrl()+"#"+carouselPicture_Redis.getBgImg());
						urlMap.put("img", url);
						urlMap.put("pageUrl", pageUrl);
						urlMap.put("bgImg", bgImg);
						
						urlList.add(urlMap);
						redisIndex++;
					}
					json.setData(urlList);
					json.setSuc();
				}
			}
			
		}catch(Exception e) {
			e.printStackTrace();
			json.setMsg("操作失败,uuid="+uuid);
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",carouselPicture"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}

	@SuppressWarnings("unchecked")
	@RequestMapping(value = "/tenTab.do", method = RequestMethod.POST)
    @ResponseBody
	public Json tenTab() {
		long startTime = System.currentTimeMillis();
		String uuid=UUID.randomUUID().toString();
		String title="获取首页10个标签,"+uuid;
		logger.info(title+",tenTab"+Constant.METHOD_BEGIN);
		Json json=new Json ();
		try {
			if(redisBean.exists(RedisKey.INDEX_TEN_TAB)) {
				byte[] bs = redisBean.get(RedisKey.INDEX_TEN_TAB.getBytes());
				ByteArrayInputStream bis = new ByteArrayInputStream(bs);
		        ObjectInputStream inputStream = new ObjectInputStream(bis);
		        List<Picture> pictureList = (List<Picture>) inputStream.readObject();
		        json.setData(pictureList);
				json.setSuc();
				inputStream.close();
			    bis.close();
			}else {
				List<Picture> pictureServiceList=pictureService.getTenTab(Constant.PICTURE_TYPE_TEN_TAB);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(bos);
				json.setData(pictureServiceList);
				json.setSuc();
				oos.writeObject(pictureServiceList);
				byte[] byteArray = bos.toByteArray();
				redisBean.set(RedisKey.INDEX_TEN_TAB.getBytes(), byteArray); 
				oos.close();
				bos.close();
			}
		}catch(Exception e) {
			e.printStackTrace();
			json.setMsg("操作失败,uuid="+uuid);
			json.setSattusCode(StatusCode.PROGRAM_EXCEPTION);
		}
		
		logger.info(title+",tenTab"+Constant.METHOD_END);
		long endTime = System.currentTimeMillis();
		logger.info(title+"耗时{0}", endTime-startTime);
		return json;
	}
	
	
	
}
