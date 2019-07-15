package com.temporary.center.ls_service.controller;

import com.temporary.center.ls_common.Constant;
import com.temporary.center.ls_common.LogUtil;
import com.temporary.center.ls_common.RedisBean;
import com.temporary.center.ls_common.RedisKey;
import com.temporary.center.ls_service.common.Json;
import com.temporary.center.ls_service.common.StatusCode;
import com.temporary.center.ls_service.dao.CarouselPictureMapper;
import com.temporary.center.ls_service.domain.CarouselPicture;
import com.temporary.center.ls_service.domain.Picture;
import com.temporary.center.ls_service.service.PictureService;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
	
	private static final Logger logger = LoggerFactory.getLogger(IndexController.class);

	@Autowired
	private RedisBean redisBean;
	
	@Autowired
	private CarouselPictureMapper carouselPictureService;
	
	@Autowired
	private PictureService pictureService;
	@Value("${staticUrlPath}")
	private String staticUrlPath;
	
	
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
			List<CarouselPicture> pictures = carouselPictureService.selectAll();
			List<JSONObject> list = new ArrayList<>();
			pictures.forEach(p->{
				JSONObject obj = new JSONObject();
				obj.put("img",staticUrlPath+p.getUrl());
				obj.put("pageUrl",p.getPageUrl());
				obj.put("bgImg",staticUrlPath+p.getBgImg());
				list.add(obj);

			});
			json.setData(list);
			json.setSuc();
			
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
				List<Picture> pictureServiceList=pictureService.getTenTab(Constant.PICTURE_TYPE_TEN_TAB);
				ByteArrayOutputStream bos = new ByteArrayOutputStream();
		        ObjectOutputStream oos = new ObjectOutputStream(bos);
				json.setData(pictureServiceList);
				json.setSuc();
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
